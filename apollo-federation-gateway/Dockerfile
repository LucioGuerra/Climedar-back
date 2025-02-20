FROM docker.io/oven/bun:latest

WORKDIR /app

COPY package.json ./

RUN bun install

COPY . .

EXPOSE 4000


CMD ["bun", "start"]