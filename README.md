[![pipeline status](https://services.pfem.clermont.inrae.fr/gitlab/peakforest/peakforest-rest-impl/badges/dev/pipeline.svg)](https://services.pfem.clermont.inrae.fr/gitlab/peakforest/peakforest-rest-impl/commits/dev)
[![coverage report](https://services.pfem.clermont.inrae.fr/gitlab/peakforest/peakforest-rest-impl/badges/dev/coverage.svg)](https://services.pfem.clermont.inrae.fr/gitlab/peakforest/peakforest-rest-impl/commits/dev)

# PeakForest - REST Implementation

## Metadata

* authors: <nils.paulhe@inrae.fr>
* creation date: `2020-07-30`
* main usage: This project is used to manage PeakForest REST webservices' OpenApi v3 implementation

## Getting Started

This project uses and requires:

- java JVM 1.8+
- maven 4+
- MySQL 5+

Note: this project is an implementation of the [PForest - REST Specifications](https://services.pfem.clermont.inrae.fr/gitlab/peakforest/peakforest-rest-spec) one.

### Prerequisites

Use [STS](https://spring.io/tools) IDE.

### Installing

- get project data `git clone git@services.pfem.clermont.inrae.fr:peakforest/peakforest-rest-impl.git`
- Build command `mvn clean install` produce:
   - `peakforest-rest-X.jar` (java bin file, ready to be run in command line; `X` matching the current version number)

## Running the tests

TODO

## Tests on local server

To test the REST application, you can call it using`curl` commands.

```bash
# test if the API is up
curl -H "Accept: application/json" http://localhost:8090/
```

The path other than `/` are restricted for confirmed user. 
We use `peakforest token` for authentication. 
The authentication token can be send as a GET parameter (key: `token`) or as in the HTTP header `Authorization` (key: `X-API-KEY`).

```bash
# count the number of compounds in the database...
# ... using HTTP header 'Authorization' ...
curl -H "Accept: application/json" -H "Authorization: X-API-KEY xxxxx" http://localhost:8090/compounds/count

# ... using HTTP header 'X-API-KEY' ...
curl -H "Accept: application/json" -H "X-API-KEY: xxxxx" http://localhost:8090/compounds/count

# ... using HTTP GET parameter 'token'
curl -H "Accept: application/json" http://localhost:8090/compounds/count?token=xxxxx

# example get compound by InChIKey
curl -H "Accept: application/json" -H "Authorization: X-API-KEY xxxxx" http://localhost:8090/compound/ZZVUWRFHKOJYTH-UHFFFAOYSA-N

# example get compound by ID
curl -H "Accept: application/json" -H "Authorization: X-API-KEY xxxxx" http://localhost:8090/compound/PFc000186

# example get spectrum by ID
curl -H "Accept: application/json" -H "Authorization: X-API-KEY xxxxx" http://localhost:8090/spectrum/PFs000025

# example get subbank file
curl -X GET \
	-H "Accept: application/octet-stream" \
	-H "X-API-KEY: xxxxx" \
	http://localhost:8090/spectra/fullscan-lcms/subbank \
	-o /tmp/myfile.txt
```

## Developers notes

To update this application from the ref. YAML file use the following commands

```bash
# build/install the project containing the ref. YAML file
cd ~/Workspace/peakforest-rest-openapi-file
mvn -Dmaven.test.skip=true clean install

# build/install the project that create the REST code Specifications from the ref. YAML file
cd ~/Workspace/peakforest-rest-spec
mvn -Dmaven.test.skip=true clean dependency:unpack install

# build this project
cd ~/Workspace/peakforest-rest-impl
mvn -Dmaven.test.skip=true clean package
```

<!-- 
### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [GitLab](https://services.pfem.clermont.inrae.fr/gitlab/your/project) for versioning. 
For the versions available, see the [tags on this repository](https://services.pfem.clermont.inrae.fr/gitlab/your/project/tags). 

## Authors

* **Firstname lastname** - *Initial work* - 

See also the list of [contributors](https://services.pfem.clermont.inrae.fr/gitlab/your/projectcontributors) who participated in this project.

## License

This project is licensed under the XXX License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
-->
