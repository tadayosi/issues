FROM jboss/base-jdk:8

USER root

# ssh
#RUN yum -y update; yum clean all
RUN yum -y install openssh-server; yum clean all
RUN mkdir /var/run/sshd

RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key -N ''

# user 'fuse'
RUN useradd fuse
RUN echo "fuse:fuse" | chpasswd

# for fabric
RUN yum -y install sudo which curl unzip; yum clean all

# SSH, Karaf-ssh, Web, rmiServerPort, rmiRegistry, ActiveMQ
EXPOSE 22 8101 8181 44444 1099 61616

ENTRYPOINT ["/usr/sbin/sshd", "-D"]
