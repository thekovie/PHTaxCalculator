# data types
# n <- 100
# i <- 100L
# c <- 100i
# ch <- '100'
# l <- TRUE
# print(class(n))
# print(class(i))
# print(class(c))
# print(class(ch))
# print(class(l))

# conditionals
# i <- 1
# switch(i,
#     '1' = print('one'),
#     '2' = print('two'),
#     '3' = print('three'),
#     print('other')
# )
# i <- 5
# if (i == 1) {
#     print('one')
# } else if (i == 2) {
#     print('two')
# } else if (i == 3) {
#     print('three')
# } else {
#     print('other')
# }

# loops
# sum <- 0
# for (i in 1:10) {
#     sum <- sum + i
# }
# print(sum)

# i <- 1
# while (i <= 10) {
#     print(i)
#     i <- i + 1
# }

# function

# pow <- function(x, y) {
#     return(x ^ y)
# }
# print(pow(2, 3))

# vector
# arr <- c(1, 2, 3, 4, 5)
# print(arr)

# Define a constructor for the Car class
Car <- function(make, model, year) {
  car <- list(make = make, model = model, year = year)
  class(car) <- "Car"
  car
}

# Define a custom print method for the Car class
print.Car <- function(car) {
  cat("Car:\n")
  cat("  Make: ", car$make, "\n")
  cat("  Model:", car$model, "\n")
  cat("  Year: ", car$year, "\n")
}

# Create a new Car object
my_car <- Car("Toyota", "Innova", 2021)
print.Car(my_car)