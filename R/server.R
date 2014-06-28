library(shiny)
library(rJava)
library(reshape2)
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
 
 #p8 <- nPlot(val ~ t, group =  'var', data = dat, 
 #type = 'stackedAreaChart')
 #p8$addParams(height = 300, dom = 'chart2')
 #return(p8)

### Simulate some data

### 3 Factor Variables
FacVar1 = as.factor(rep(c("level1", "level2"), 25))
FacVar2 = as.factor(rep(c("levelA", "levelB", "levelC"), 17)[-51])
FacVar3 = as.factor(rep(c("levelI", "levelII", "levelIII", "levelIV"), 13)[-c(51:52)])

### 4 Numeric Vars
set.seed(123)
NumVar1 = round(rnorm(n = 50, mean = 1000, sd = 50), digits = 2)  ### Normal distribution
set.seed(123)
NumVar2 = round(runif(n = 50, min = 500, max = 1500), digits = 2)  ### Uniform distribution
set.seed(123)
NumVar3 = round(rexp(n = 50, rate = 0.001))  ### Exponential distribution
NumVar4 = 2001:2050

simData = data.frame(FacVar1, FacVar2, FacVar3, NumVar1, NumVar2, NumVar3, NumVar4)
bpf3 = data.frame(table(simData$FacVar3))
print(bpf3)
bpf3$Var1 <- paste(bpf3$Var1,"",sep="")
for(i in 1:nrow(bpf3)) {
    print(identical(bpf3[i,1],"levelI"))
    if(identical(bpf3[i,1],"levelI")){
      bpf3[i,1] <- paste(bpf3[i,1],"green",sep=":") 
    }else{
      bpf3[i,1] <- paste(bpf3[i,1],"orange",sep=":") 
    }
}
p2 = nPlot(x = "Var1", y = "Freq", data = bpf3, type = "discreteBarChart")
p2$addParams(height = 300, dom = 'chart2')
print(bpf3)



p2$chart(color = "#! function(d, x){ var color = d.Var1; return color.split(':')[1];} !#")
return(p2)
  })


output$metaspace <- renderChart({
  #print("Render MetaSpace")
  metaSpaceObserver <- .jnew("com/rxjava/jmx/MetaSpaceObserver")
  .jcall(metaSpaceObserver,"V","consume")
  init <- .jcall(metaSpaceObserver ,"J","getInit")
  committed <- .jcall(metaSpaceObserver ,"J","getCommitted")
  metacapacity <- data.frame(lapply(as.Date('2014-08-06') + 0:0, seq, as.Date('2014/10/08'), '1 weeks'));
  metacapacity['init'] <- init
  metacapacity['committed'] <- committed
  colnames(metacapacity) <- c("date","init","committed")
  metacapacity  <- transform(metacapacity,date=as.character(date))
  d_long = melt(metacapacity, id = "date")
  ms = hPlot(x = "date", y = "value", group = "variable", type = "area", data = d_long)
  ms$yAxis(title = list(text = "Committed(MB)"))
  ms$xAxis(title = list(text = "Date"))
ms$chart(zoomType = "x")

ms$plotOptions(
  line = list(
    marker = list(enabled = F)
  )
)
  ms$addParams(height = 300, dom = 'metaspace')
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
  ygen  <- transform(ygen,date=as.character(date))
  d_long = melt(ygen, id = "date")
  yg = hPlot(x = "date", y = "value", group = "variable", type = "area", data = d_long,title="Eden/Survivor spaces",radius=0)
  yg$plotOptions(series=list(marker=list(enabled='false')),area  = list(marker = list(states= list(hover=list(enabled='false')))))
  yg$colors(
  'rgba(223, 83, 83, .2)', 
  'rgba(60, 179, 113, .2)', 
  'rgba(238, 130, 238, .2)'
)
  yg$addParams(height = 300, dom = 'younggen')
  yg$yAxis(title = list(text = "MB"))
  yg$xAxis(title = list(text = "Date"))
  return(yg)
  })

output$test <- renderChart({
   month=as.factor(c(1:12))
   set.seed(123)
   xseries=rnorm(12)*50
   set.seed(123)
   zseries=rnorm(12)*(-75)
   df=data.frame(month,xseries,zseries)
   meltdf=melt(df,id="month")
   myplot=hPlot(x="month",y="value",group="variable",type="area",data=meltdf)
   myplot$plotOptions(series=list(marker=list(enabled='false')))
   myplot$colors('rgba(0,255,0,.3)', 'rgba(255,0,255,.3)')
   myplot $addParams(height = 300, dom = 'test')
   return(myplot)
})


 realtimeloadeddata <- function(df,yd){
         if( yd > nrow(df)){
             return
         }
         occupant <- realtimeyoungdata()
         #print(paste("User[", occupant[1],"] Max [", occupant[2],"]"))
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




