package com.example.demoapp.constants

class EnvironmentConstants {
    companion object {

        const val BASE_URL = "http://api.nytimes.com/svc/mostpopular/v2/"
        const val PATH_ALL_SECTIONS = "all-sections"
        const val PATH_PERIOD_SEVEN = "7.json"
        const val PATH_MOST_VIEWED = "mostviewed/{section}/{period}"
    }
}