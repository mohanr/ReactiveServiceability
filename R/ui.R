library(shiny)

shinyUI(fluidPage(theme = "/shared/bootstrap/css/bootstrap.css",

  titlePanel("Legerdemain"),

  navlistPanel(
    "Garbage Collection",
    tabPanel("Generations",
      h3("Heap")
    ),
    tabPanel("Threads",
      h3("Threads and Locks")
    ),
    "-----",
    tabPanel("JIT",
      h3("Methods")
    )
  )
))