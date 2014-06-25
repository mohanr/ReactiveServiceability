library(shiny)
library(rJava)
require(rCharts)
options(RCHART_WIDTH = 800)
.jinit(parameters="-Xmx512m")
shinyServer(function(input, output, session) {
print(.jclassPath())
autoInvalidate <- reactiveTimer(5000, session)


output$chart2 <- renderChart({
dat <- data.frame(
  t = rep(0:1, each = 4), 
  var = rep(LETTERS[1:4], 4), 
  val = round(runif(4*24,0,50))
)
 
p8 <- nPlot(val ~ t, group =  'var', data = dat, 
 type = 'stackedAreaChart')
    p8$addParams(height = 300, dom = 'chart2')
    return(p8)
  })


output$metaspace <- renderChart({
  print("Render MetaSpace")
  metaSpaceObserver <- .jnew("com/rxjava/jmx/MetaSpaceObserver")
  .jcall(metaSpaceObserver,"V","consume")
  init <- .jcall(metaSpaceObserver ,"J","getInit")
  committed <- .jcall(metaSpaceObserver ,"J","getCommitted")
  metacapacity <- data.frame(lapply(as.Date('2014-08-06') + 0:0, seq, as.Date('2014/10/08'), '1 weeks'));
  metacapacity['init'] <- init
  metacapacity['committed'] <- committed
  colnames(metacapacity) <- c("date","init","committed")
  metacapacity  <- transform(metacapacity,date=as.character(date))
  ms <- mPlot(x = "date", y = c("init", "committed","used","max"), type = "Area", data = metacapacity)
  ms$addParams(height = 300, dom = 'metaspace')
  ms$set(title="MetaSpace")
  return(ms)
  })

  #Global variables.This is essential.
  ydata <<- 1
  ygen <<- youngdata()

output$younggen <- renderChart({
  autoInvalidate()
  ydata <<- ydata + 1
  #print(ygen)
  #ygen <<- loadeddata(ygen,ydata)
  ygen <<- realtimeloadeddata(ygen,ydata)
  yg <- mPlot(x = "date", y = c("eden", "survivor"), type = "Line", data = ygen)
  yg$addParams(height = 300, dom = 'younggen')
  yg$set(title="Younggen")
  yg$set( hideHover = "auto" )
  yg$set( pointSize = 0)
  return(yg)
  })

 realtimeloadeddata <- function(df,yd){
         if( yd > nrow(df)){
             return
         }
         occupant <- realtimeyoungdata()
         print(paste("User[", occupant[1],"] Max [", occupant[2],"]"))
         newdata <- data.frame(occupant[1], occupant[2],stringsAsFactors=FALSE)
         colnames(newdata) <- c("eden","survivor")    
         df[yd,which(names(df) %in% names(newdata))]  <- newdata
 return(df)
 }

 loadeddata <- function(df,yd){
         if( yd > nrow(df)){
             return
         }
         newdata <- data.frame(sample(1:40, 1, replace=F), sample(1:40, 1, replace=F),stringsAsFactors=FALSE)
         colnames(newdata) <- c("eden","survivor")    
         df[yd,which(names(df) %in% names(newdata))]  <- newdata
 return(df)
 }

})

realtimeyoungdata <- function(){
  realtimeyoungdataobserver <- .jnew("com/rxjava/jmx/YoungGenPeriodicObserver")
  .jcall(realtimeyoungdataobserver,"V","consume")
  occupancy <- .jcall(realtimeyoungdataobserver ,"Lcom/rxjava/jmx/YoungGenOccupancy;","getOccupancy")
  used <- occupancy$getUsed()
  max <- occupancy$getMax()
  print(paste("User[", used,"] Max [", max,"]"))
  result <- c(used, max) 
  return(result) 
}

youngdata <- function() {
  younggen <- data.frame(lapply(as.Date('2014-08-06') + 0:0, seq, as.Date('2014/10/08'), '1 days'));
  younggen['eden'] <- 0
  younggen['survivor'] <- 0
  colnames(younggen) <- c("date","eden","survivor")
  return(younggen)
}




