 #!/bin/sh
#rm -rf /root/.ssh &&\
#mkdir /root/.ssh &&\
#cp -R /root/ssh/* /root/.ssh/ && \
#chmod -R 600 /root/.ssh/* && \
#chmod 600 staging_ec2_pair.pem &&\
#nohup ssh \
#-i staging_ec2_pair.pem \
#-vv \
#-o StrictHostKeyChecking=no \
#-N 54.218.79.125 \
#-L *:5432:riggo-io-staging.c3utpceskpit.us-west-2.rds.amazonaws.com:5432 \
#-L *:6379:stagingrediscluster.mbot9e.0001.usw2.cache.amazonaws.com:6379 \
#-l ec2-user  &
    
    while sleep 60; do
    
    echo "HANG IN THERE."
  
    done

