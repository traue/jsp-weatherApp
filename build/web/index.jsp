<%@page import="weather.pojo.WeatherApp"%>
<%@page import="weather.http.Http"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    request.setCharacterEncoding("UTF-8");
    WeatherApp weather = null;
    String city = "";

    if (request.getParameter("city") != null) {
        city = request.getParameter("city");
        weather = Http.getClima(city);
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>WeatherApp</title>
        <link rel="icon" type="image/png" href="img/logo.png" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="./">WeatherApp</a>
                <form class="d-flex" method="post">
                    <input class="form-control me-2" name="city" type="search" 
                           placeholder="City"
                           value="<%=city%>">
                    <button class="btn btn-outline-info" type="submit">Search</button>
                </form>
            </div>
        </nav>

        <% if (weather != null) {%>

        <div class="h-100 d-flex align-items-center justify-content-center" style="margin-top: 120px;">
            <div class="card text-center" style="width: 25rem;">
                <div class="card-body">
                    <h1 class="card-title"><%= String.format("%.0f", Math.ceil(weather.getMain().getTemp()))%>ºC</h1>
                    <img src="https://openweathermap.org/img/wn/<%= weather.getWeather().get(0).getIcon()%>@2x.png">
                    <p class="card-text">
                    <p class="text-capitalize"><b><%= weather.getWeather().get(0).getDescription()%></b></p>
                    <b>Fells like:"</b> <%= String.format("%.0f", Math.ceil(weather.getMain().getFeelsLike()))%>º<br>
                    <b>Min:</b> <%= String.format("%.0f", Math.ceil(weather.getMain().getTempMin()))%>º<br>
                    <b>Max:</b> <%= String.format("%.0f", Math.ceil(weather.getMain().getTempMax()))%>º<br>
                    <b>Humidity:</b> <%= weather.getMain().getHumidity()%>%<br>
                    <b>Visibility:</b> <%= weather.getVisibility()%>Km<br>
                    <b>Pressure:</b> <%=weather.getMain().getPressure()%>hPa<br>
                    </p>
                </div>
            </div>
        </div>

        <% } else {%>
        <div class="h-100 d-flex align-items-center justify-content-center" style="margin-top: 120px;">
            <div class="card text-center" style="width: 25rem;">
                <div class="card-body">
                    <h4 class="card-title"><%= city.equals("") ? "Search for a city" : "Unknown city"%></h4>
                </div>
            </div>
        </div>
        <% }%>
    </body>
</html>