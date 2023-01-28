
# About            
docker image for SMPP simulator from http://www.seleniumsoftware.com/              


# usage           
1. build:
```sh
docker build -t smpp/smppsim:<tag> .
```

2. run:
```sh
docker run -it -P smpp/smppsim     
```

# docker-compose说明
在项目目录smpp_server_docker执行
```sh
docker build -t smppsim:0.1 .
docker-compose up -d