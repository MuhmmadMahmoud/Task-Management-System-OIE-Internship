# ===== step 1: build the Angular app with Node =====
FROM node:22-alpine AS build
WORKDIR /app

# copy the package files alone first so Docker caches the npm install
COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build

# ===== step 2: give the built files with nginx =====
# Angular becomes normal html, css and js files, so we only need a small web server
FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
