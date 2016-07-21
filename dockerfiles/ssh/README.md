# SSH Docker Container

    $ cat <id_rsa.pub> > authorized_keys
    $ docker build --rm -t ssh .
    $ docker run -d -p 2022:22 -p 8101:8101 -p 8181:8181 -p 44444:44444 -p 1099:1099 -p 61616:61616 --name ssh ssh
