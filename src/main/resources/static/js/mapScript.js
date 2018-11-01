var map = L.map("map").setView([53.3419, -6.2827], 13);

L.tileLayer(
    'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw',
    {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
            '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
            'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id: 'mapbox.streets'
    }).addTo(map);

var myIcon = L.icon({
    iconUrl: 'images/unicon.png',
    iconSize: [50, 80],
    iconAnchor: [28, 80],
    popupAnchor: [-3, -76]
});

var markers = [];

function getUnicorns() {
    $.ajax({
        type: "get",
        url: "/unicorn/recent",
        success: function (data, text) {
            console.log(data);
            // Remove old markers
            for (var j = 0; j < markers.length; j++) {
                map.removeLayer(markers[j]);
            }
            markers.length = 0;

            // Create new markers
            for (var i = 0; i < data.length; i++) {
                var unicorn = data[i];
                var latitude = unicorn["latitude"];
                var longitude = unicorn["longitude"];
                var name = unicorn["name"];
                var distance = unicorn["distance"];
                var health = unicorn["healthPoints"];
                var magic = unicorn["magicPoints"];
                var updateTime = unicorn["statusTime"];
                var marker = L.marker([longitude, latitude], {icon: myIcon});
                var tooltip = L.tooltip({sticky: true});
                tooltip.setContent(
                    '<b>Name: </b>' + name +
                    '</br><b>Distance: </b>' + distance +
                    '</br><b>Latitude: </b>' + latitude +
                    '</br><b>Longitude: </b>' + longitude +
                    '</br><b>Health: </b>' + health +
                    '</br><b>Magic: </b>' + magic +
                    '</br><b>Last Update: </b>' + updateTime
                );
                marker.bindTooltip(tooltip).openTooltip();
                marker.addTo(map);
                markers.push(marker);
            }
        },
        error: function (request, status, error) {
            console.log(request.responseText);
        }
    });
}

var interval = self.setInterval(function(){getUnicorns()}, 3000);
