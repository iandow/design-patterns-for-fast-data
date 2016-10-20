# Design Patterns for working with Fast Data in Kafka

This is repo contains all the resources for my *Design Patterns for Fast Data* presentation.

## Abstract:

Apache Kafka is an open-source message broker project that provides a platform for storing and processing real-time data feeds. In this presentation I describe the concepts that are important to understand in order to effectively use the Kafka API. You will see how to prepare a development environment from scratch, how to write a basic publish/subscribe application, and how to run it on a variety of different cluster types, starting with a single-node cluster on Virtual Box, then on multi-node cluster using Heroku’s “Kafka as a Service”, and finally on an enterprise-grade multi-node cluster using MapR’s Converged Data Platform.  

I also discuss strategies for working with "fast data" by describing which Kafka configurations and data types have the largest impact on performance, and I provide some useful JUnit tests which combined with statistical analysis in R, help quantify how various Kafka configurations effect throughput.
