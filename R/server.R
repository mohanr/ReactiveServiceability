library(shiny)
library(rJava)
.jinit(parameters="-Xmx512m")
shinyServer(function(input, output) {
print(.jclassPath())
})
