package com.example.taskmanager;

import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// the steps call the real API, so one scenario tests all the layers together
public class TaskManagerStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ResponseEntity<String> lastResponse;

    private String currentToken;

    // the id of every task I create, so the steps can use the title instead
    private Map<String, Long> taskIds = new HashMap<>();

    // every scenario starts with an empty database
    @Before
    public void cleanTheDatabase() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        currentToken = null;
        lastResponse = null;
        taskIds = new HashMap<>();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (currentToken != null) {
            headers.set("Authorization", "Bearer " + currentToken);
        }
        return headers;
    }

    private JsonNode responseAsJson() {
        try {
            return objectMapper.readTree(lastResponse.getBody());
        } catch (Exception e) {
            throw new RuntimeException("The response is not a json: " + lastResponse.getBody());
        }
    }

    // ================= registration and login steps =================

    @Given("there is no user with username {string}")
    public void thereIsNoUserWithUsername(String username) {
        // the database is already empty, this step is only for reading
        assertTrue(userRepository.findByUsername(username).isEmpty());
    }

    @When("I register with username {string}, email {string} and password {string}")
    public void iRegisterWith(String username, String email, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("email", email);
        body.put("password", password);

        lastResponse = restTemplate.postForEntity(url("/api/auth/register"),
                new HttpEntity<>(body, buildHeaders()), String.class);
    }

    @Given("a user is registered with username {string}, email {string} and password {string}")
    public void aUserIsRegisteredWith(String username, String email, String password) {
        iRegisterWith(username, email, password);
        assertEquals(201, lastResponse.getStatusCode().value());
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWith(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        lastResponse = restTemplate.postForEntity(url("/api/auth/login"),
                new HttpEntity<>(body, buildHeaders()), String.class);
    }

    @Given("I am logged in as {string} with password {string}")
    public void iAmLoggedInAs(String username, String password) {
        currentToken = null;
        iLoginWith(username, password);
        assertEquals(200, lastResponse.getStatusCode().value());
        currentToken = responseAsJson().get("token").asText();
    }

    @Given("I am not logged in")
    public void iAmNotLoggedIn() {
        currentToken = null;
    }

    @Given("I am logged in with a wrong token")
    public void iAmLoggedInWithAWrongToken() {
        currentToken = "this.is.not.a.real.token";
    }

    @Then("I should receive a token")
    public void iShouldReceiveAToken() {
        JsonNode json = responseAsJson();
        assertNotNull(json.get("token"));
        assertTrue(json.get("token").asText().length() > 0);
    }

    // ================= task steps =================

    @When("I create a task with title {string}, status {string} and priority {string}")
    public void iCreateATaskWith(String title, String status, String priority) {
        Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("description", "created from the BDD test");
        body.put("status", status);
        body.put("priority", priority);

        lastResponse = restTemplate.postForEntity(url("/api/tasks"),
                new HttpEntity<>(body, buildHeaders()), String.class);
    }

    @When("I create a task with title {string} without status and priority")
    public void iCreateATaskWithoutStatusAndPriority(String title) {
        Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("description", "created from the BDD test");

        lastResponse = restTemplate.postForEntity(url("/api/tasks"),
                new HttpEntity<>(body, buildHeaders()), String.class);
    }

    @Given("I have a task with title {string}, status {string} and priority {string}")
    public void iHaveATaskWith(String title, String status, String priority) {
        iCreateATaskWith(title, status, priority);
        assertEquals(201, lastResponse.getStatusCode().value());
        // save the id for the next steps
        taskIds.put(title, responseAsJson().get("id").asLong());
    }

    @When("I ask for all my tasks")
    public void iAskForAllMyTasks() {
        lastResponse = restTemplate.exchange(url("/api/tasks"), HttpMethod.GET,
                new HttpEntity<>(buildHeaders()), String.class);
    }

    @When("I ask for the task {string}")
    public void iAskForTheTask(String title) {
        Long id = taskIds.get(title);
        lastResponse = restTemplate.exchange(url("/api/tasks/" + id), HttpMethod.GET,
                new HttpEntity<>(buildHeaders()), String.class);
    }

    @When("I ask for the task with id {long}")
    public void iAskForTheTaskWithId(Long id) {
        lastResponse = restTemplate.exchange(url("/api/tasks/" + id), HttpMethod.GET,
                new HttpEntity<>(buildHeaders()), String.class);
    }

    @When("I update the task {string} to title {string}, status {string} and priority {string}")
    public void iUpdateTheTask(String oldTitle, String newTitle, String status, String priority) {
        Long id = taskIds.get(oldTitle);

        Map<String, String> body = new HashMap<>();
        body.put("title", newTitle);
        body.put("description", "updated from the BDD test");
        body.put("status", status);
        body.put("priority", priority);

        lastResponse = restTemplate.exchange(url("/api/tasks/" + id), HttpMethod.PUT,
                new HttpEntity<>(body, buildHeaders()), String.class);
    }

    @When("I delete the task {string}")
    public void iDeleteTheTask(String title) {
        Long id = taskIds.get(title);
        lastResponse = restTemplate.exchange(url("/api/tasks/" + id), HttpMethod.DELETE,
                new HttpEntity<>(buildHeaders()), String.class);
    }

    @When("I filter my tasks by status {string}")
    public void iFilterMyTasksByStatus(String status) {
        lastResponse = restTemplate.exchange(url("/api/tasks?status=" + status), HttpMethod.GET,
                new HttpEntity<>(buildHeaders()), String.class);
    }

    @When("I filter my tasks by priority {string}")
    public void iFilterMyTasksByPriority(String priority) {
        lastResponse = restTemplate.exchange(url("/api/tasks?priority=" + priority), HttpMethod.GET,
                new HttpEntity<>(buildHeaders()), String.class);
    }

    @When("I filter my tasks by status {string} and priority {string}")
    public void iFilterMyTasksByStatusAndPriority(String status, String priority) {
        lastResponse = restTemplate.exchange(url("/api/tasks?status=" + status + "&priority=" + priority),
                HttpMethod.GET, new HttpEntity<>(buildHeaders()), String.class);
    }

    // ================= checking steps =================

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        assertEquals(expectedStatus, lastResponse.getStatusCode().value());
    }

    @Then("the error message should be {string}")
    public void theErrorMessageShouldBe(String expectedMessage) {
        assertEquals(expectedMessage, responseAsJson().get("message").asText());
    }

    @Then("I should get {int} tasks")
    public void iShouldGetTasks(int expectedCount) {
        assertEquals(expectedCount, responseAsJson().size());
    }

    @Then("the task in the response should have title {string}")
    public void theTaskShouldHaveTitle(String expectedTitle) {
        assertEquals(expectedTitle, responseAsJson().get("title").asText());
    }

    @And("the task in the response should have status {string}")
    public void theTaskShouldHaveStatus(String expectedStatus) {
        assertEquals(expectedStatus, responseAsJson().get("status").asText());
    }

    @And("the task in the response should have priority {string}")
    public void theTaskShouldHavePriority(String expectedPriority) {
        assertEquals(expectedPriority, responseAsJson().get("priority").asText());
    }
}
