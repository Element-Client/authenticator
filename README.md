# VeriMe
VeriMe is a brand new, open source 2 factor authentication system that backs up authentication codes to your own server. In this branch, we preserve the java libraries and source code.

# VeriMe: Android Support
Android support is already available for VeriMe. All of it exists in the verime.tasks package. I have even made a base for you to extend: "DataProcessorTask". Just be sure to add useLibrary: 'org.apache.http.legacy' to your app level build.gradle, as well as compile group: 'org.apache.httpcomponents' , name: 'httpclient-android' , version: '4.3.5.1' . These are needed by HttpClient.