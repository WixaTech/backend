# qr-code-encoder

# env variables
ACCESS_KEY - aws access key
SECRET_KEY - aws secret key

# building container and pushing to ecr
./gradlew build

docker build --tag=qr-code:latest .
``
docker run -d -p 8080:8080 qr-code:latest -e ACCESS_KEY=xxxxxx -e SECRET_KEY=xxxxxx

aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 242422306324.dkr.ecr.us-east-1.amazonaws.com

docker tag qr-code:latest 242422306324.dkr.ecr.us-east-1.amazonaws.com/qr-code:latest

docker push 242422306324.dkr.ecr.us-east-1.amazonaws.com/qr-code:latest

# in aws

update environment variables of container which are included in Paramater Store

# heroku

`git push heroku main

`heroku logs --tail``

`heroku ps:scale web=1`