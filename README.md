# Helpers
Contains Helping commands for the Production Grades

## Google Cloud

```bash
gcloud init
gcloud auth login
gcloud config set project YOUR_PROJECT_ID
gcloud app create --region=asia-south1
```

```python
from flask import Flask

app = Flask(__name__)

@app.route("/")
def hello():
    return "Hello World from Google App Engine using Python!"

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8080)
```

```text
Flask
```

```yaml
runtime: python314'
```

## CloudSim

```bash
git clone https://github.com/cloudsimplus/cloudsimplus-examples.git
./mvnw clean install
```

```bash
mvn compile
```
```bash
mvn exec:java -Dexec.mainClass="org.cloudsimplus.examples.BasicFirstExample"
```

## Hadoop

```bash
mkdir -p ~/wordcount
cd ~/wordcount
nano input.txt
```
```bash
hdfs dfs -mkdir -p /user/$USER/wordcount/input
```

```bash
hdfs dfs -put input.txt /user/$USER/wordcount/input
```

```bash
hdfs dfs -ls /user/$USER/wordcount/input
```

```bash
find "$HADOOP_HOME/share/hadoop/mapreduce" \
-name 'hadoop-mapreduce-examples-*.jar'
```

```bash
hadoop jar "$HADOOP_HOME"/share/hadoop/mapreduce/hadoop-mapreduce-examples-*.jar \
org.apache.hadoop.examples.WordCount \
/user/$USER/wordcount/input \
/user/$USER/wordcount/output
```

```bash
hadoop jar "$HADOOP_HOME"/share/hadoop/mapreduce/hadoop-mapreduce-examples-*.jar \
wordcount \
/user/$USER/wordcount/input \
/user/$USER/wordcount/output
```

## Docker

```bash
sudo groupadd docker
sodo usermod -aG docker $USER
newgrp docker
```

```bash
docker build -t multiply-app .
docker run -it --rm multiply-app
docker login
docker tag multiply-app USERNAME/multiply-app:latest
docker push USERNAME/multiply-app:latest
```

```docker
FROM python:3.12-slim

WORKDIR /app

COPY multi.py .

CMD ["python","-u","multi.py"]
```