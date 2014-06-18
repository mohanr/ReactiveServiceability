library(shiny)
library(rJava)
setwd(path.expand("~/") )
.jinit(parameters="-Xmx512m")
shinyServer(function(input, output) {
print(.jclassPath())
younggenprobe <- .jnew("com/memory/probe/YoungGenProbe")
print(.jcall(younggenprobe,"J","getYoungCapacity"))
})
