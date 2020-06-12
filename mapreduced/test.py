import jaydebeapi
import apache_beam as beam
from apache_beam.options.pipeline_options import PipelineOptions
import re
import redis
import schedule
import time
import psycopg2


def cron():
    def sort_list_tuple(listoftuple):
        listoftuple.sort(key=lambda x: x[1])
        return listoftuple

    r = redis.Redis(host='localhost', port=6379, db=0)

    # conn = jaydebeapi.connect("org.h2.Driver",  # driver class
    #                           "jdbc:h2:tcp://localhost/~/test",  # JDBC url
    #                           ["sa", ""],  # credentials
    #                           "./h2-1.4.200.jar", )  # location of H2 jar

    text = []
    try:
        conn=psycopg2.connect("dbname='postgres' user='yilongluan' host='localhost' password=''")
        curs = conn.cursor()
        # Fetch the last 10 timestamps
        curs.execute("SELECT * FROM tweet")
        data = curs.fetchall()
        print(len(data[0]))
        print(type(data[0]))
        for value in data:
            # the values are returned as wrapped java.lang.Long instances
            # invoke the toString() method to print them
            text.append(value[5])
    finally:
        if curs is not None:
            curs.close()

    pipeline_options = PipelineOptions(None)
    print(len(text))
    wordlist = []
    with beam.Pipeline(options=pipeline_options) as pipeline:
        (
                pipeline
                | beam.Create(text)
                | beam.FlatMap(lambda tweet: re.findall(r"[a-zA-Z]+", tweet))
                | beam.Map(lambda word: (word, 1))
                | beam.CombinePerKey(sum)
                | beam.ParDo(lambda count: wordlist.append(count))
        )

    sort_list_tuple(wordlist)

    pattern = "word: {}, count: {}"
    for count in wordlist:
        r.hset("hotWords", count[0], count[1])

    dic = r.hgetall("test")
    print(type(dic))
    for key, val in dic.items():
        print(key, val)


schedule.every(10).seconds.do(cron)
while True:
    schedule.run_pending()
    time.sleep(1)
