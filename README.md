Check weather for a public data set of crashes
===========

* Imagine you have started working at VicRoads Links to an external site. as a data visualisation expert. Your manager shares the following data set with you and asks you to visualise this data set to let VicRoads know what it needs to do to reduce the number (social cost) of crashes. 
* https://vicroadsopendata-vicroadsmaps.opendata.arcgis.com/datasets/74dd92127eea4404b0dad1d7e39bf0e3
* In order to explore the relationship between accidents and different weather conditions, I tried to retrieve data through weatherapi.com and combine it with this dataset.


## Tech Stack

Programming language： Java 8

* It mainly uses the Apache open source library: HttpClient.
* Based on the definition of the [History API](https://www.weatherapi.com/docs/) from weatherapi.com.
* Use thread pools to concurrently execute requests to save time.


## How to use

* After downloading the dataset from VicRoads, [preprocess](https://gist.github.com/storypanda/182fc00050b899a7e1c03481411f27ab) the columns: ACCIDENT_DATE and ACCIDENT_TIME.
* Modify the filePath in the Main method in CheckWeather.java.