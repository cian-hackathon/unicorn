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
    iconSize: [100, 100],
    iconAnchor: [22, 94],
    popupAnchor: [-3, -76]
});

L.marker([53.3419, -6.2827], {icon: myIcon}).addTo(map);