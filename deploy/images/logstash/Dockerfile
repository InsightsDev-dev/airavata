FROM logstash:2.3.4

RUN /opt/logstash/bin/plugin install logstash-output-amazon_es
RUN /opt/logstash/bin/plugin install logstash-codec-avro logstash-codec-cloudtrail logstash-input-journald
ENTRYPOINT []
