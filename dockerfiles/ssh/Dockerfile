FROM jboss/base-jdk:8

USER root

# ssh
RUN yum -y update; yum clean all
RUN yum -y install openssh-server openssh-clients; yum clean all
RUN mkdir /var/run/sshd

RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key -N ''

# for fabric
RUN yum -y install sudo which curl unzip telnet; yum clean all

# sudo
RUN echo "%wheel ALL=(ALL) ALL" >> /etc/sudoers

# user 'fuse'
RUN useradd -m fuse; echo "fuse:fuse" | chpasswd; usermod -a -G wheel fuse
RUN mkdir -p /home/fuse/.ssh; chown fuse /home/fuse/.ssh; chmod 700 /home/fuse/.ssh
ADD authorized_keys /home/fuse/.ssh/authorized_keys
RUN chown fuse /home/fuse/.ssh/authorized_keys; chmod 600 /home/fuse/.ssh/authorized_keys

# SSH, Karaf-ssh, Web, rmiServerPort, rmiRegistry, ActiveMQ
EXPOSE 22 8101 8181 44444 1099 61616

ENTRYPOINT ["/usr/sbin/sshd", "-D"]
