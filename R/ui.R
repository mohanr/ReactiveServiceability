library(shiny)
require(rCharts)
library(knitr)

shinyUI(fluidPage(theme = "/shared/bootstrap/css/bootstrap.css",

  titlePanel("Legerdemain"),

  navlistPanel(
    "Garbage Collection",
    tabPanel("Generations",
      h3("Heap"),
    tabsetPanel(
        tabPanel("Metaspace",showOutput("metaspace", "highcharts")),
        tabPanel("YoungGen",showOutput("younggen", "highcharts")),
        tabPanel("Test",showOutput("test", "highcharts"))
    )
    ),
    tabPanel("Threads",
      h3("Threads and Locks")
    ),
    "-----",
    tabPanel("JIT",
      h3("Methods"),
      tags$style(type="text/css",
      ".shiny-output-error { visibility: hidden; }",
      ".shiny-output-error:before { visibility: hidden; }"
     ),
      showOutput("chart2", "nvd3")
    )
)))