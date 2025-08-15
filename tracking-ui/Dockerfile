FROM node:20-alpine AS build

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH

COPY package*.json ./

ARG REACT_APP_API_INFO_URL
ENV REACT_APP_API_INFO_URL=${REACT_APP_API_INFO_URL}
ARG REACT_APP_API_SOCKET_URL
ENV REACT_APP_API_SOCKET_URL=${REACT_APP_API_SOCKET_URL}

RUN npm ci
COPY . .

RUN npm run build

########################

FROM node:20-alpine

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH

COPY package*.json ./
RUN npm ci --omit=dev
COPY --from=build /app/build ./build

EXPOSE 3000
CMD ["npx", "react-router-serve", "./build/server/index.js"]
