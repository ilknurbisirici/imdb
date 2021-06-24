# imdb

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This project is based on the popular website IMDb which offers movies and TV shows information.  

* You can find genres which is given a query by the user, where he/she provides an actor/actress name, the system should determine if that person has become typecasted (at least half of their work is one genre).
*  You can find the application replies with a list of movies or TV shows that both people have shared whisch is given a query by the user, where the input is two actors/actresses names, 
* You can find  what’s the degree of separation between the person (e.g. actor or actress) the user has entered and Kevin Bacon.

### Built With

* Spring Boot
* Spring Batch
* H2 Database


## Getting Started



### Prerequisites

*Java 8
*Any editor you can choose

### Installation

1. Clone the repository: git clone https://github.com/ilknurbisirici/imdb.git
2. cd imdb
3. mvn clean install
And that's all. You can run the project.



<!-- USAGE EXAMPLES -->
## Usage

After run the project you can import and use postman collection: https://www.getpostman.com/collections/4426ab618637b840115e
You can check added datas: http://localhost:8080/h2-console
You can change datas for testing from : /src/main/resources/*.tsv files




