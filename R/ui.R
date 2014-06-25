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
        tabPanel("Metaspace",showOutput("metaspace", "Morris")),
        tabPanel("YoungGen",showOutput("younggen", "Morris"))
    )
    ),
    tabPanel("Threads",
      h3("Threads and Locks")
    ),
    "-----",
    tabPanel("JIT",
      h3("Methods"),
      showOutput("chart2", "nvd3")
    )
)))