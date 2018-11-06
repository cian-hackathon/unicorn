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

var tracking = false;
var markers = [];

function getUnicorns() {
    if (tracking) {
        $.ajax({
            type: "get",
            url: "/unicorn/recent",
            success: function (data, text) {
                console.log(data);
                // Remove old markers
                clearMarkers();

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
                    var marker = L.marker([latitude, longitude], {icon: myIcon});
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
}
var interval = self.setInterval(function(){getUnicorns()}, 3000);

function clearMarkers() {
    for (var j = 0; j < markers.length; j++) {
        map.removeLayer(markers[j]);
    }
    markers.length = 0;
}

function convertFormToJSON($form){
    var array = $form.serializeArray();
    var json = {};

    $.each(array, function() {
        json[this.name] = this.value || '';
    });

    return JSON.stringify(json);
}

$("#trackButton").on("click", function() {
    if (tracking === true) {
        tracking = false;
        clearMarkers();
        $(this).css("background-color", "crimson");
        $(this).text("Tracking: Off");
    } else {
        tracking = true;
        $(this).css("background-color", "green");
        $(this).text("Tracking: On");
    }
});

$("#next").on("click", function() {
    var $form = $("#add-unicorn-form");
    var data = convertFormToJSON($form);
    console.log(data);
    $.ajax({
        url: "/unicorn",
        method: "post",
        contentType:"application/json; charset=utf-8",
        data: data,
        success: function(r){
            console.log(r);
            showToast("Unicorn added successfully!");
            $("#addUnicornModal").css("display", "none");
        },
        error: function(jqXHR, textStatus, errorThrown) {
            showToast("Failed to add unicorn. Check logs for details.");
        }
    });
    return false; // this one
});

$("#kill_button").on('click', function() {

    var $form = $("#remove-unicorn-form");
    var data = $form.serializeArray()[0].value;
    console.log(data);

    $.ajax({
        url: "/unicorn/remove" + '?name=' + data,
        method: "DELETE",
        success: function (r) {
            console.log(r);
            showToast("Unicorn killed successfully!");
            $("#killUnicornModal").css("display", "none");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            // Request failed. Show error message to user.
            // errorThrown has error message, or "timeout" in case of timeout.
            console.log(jqXHR.responseText);
            showToast("Unable to kill unicorn. Check logs and try killing harder.");
        }
    })
});

window.onload = function () {

    var l = Snap('#logo');

    var logoTitle = 'ubercorn';
    var logoRandom = '';
    var logoTitleContainer = l.text(0, '98%', '');
    var possible = "-+*/|}{[]~\\\":;?/.><=+-_)(*&^%$#@!)}";
    logoTitleContainer.attr({
        fontSize: 280,
        fontFamily: 'Metrophobic',
        fontWeight: '600',
        fill: "#fff0e3"
    });

    function generateRandomTitle(i, logoRandom) {
        setTimeout( function() {
            logoTitleContainer.attr({ text: logoRandom });
        }, i*100 );
    }

    for( var i=0; i < logoTitle.length+1; i++ ) {
        logoRandom = logoTitle.substr(0, i);
        for( var j=i; j < logoTitle.length; j++ ) {
            logoRandom += possible.charAt(Math.floor(Math.random() * possible.length));
        }
        generateRandomTitle(i, logoRandom);
        logoRandom = '';
    }
};

/*
  BEGIN MODAL LOGIC
 */
var addUnicornModal = $("#addUnicornModal");
var killUnicornModal = $("#killUnicornModal");

// Get the button that opens the modal
var addUnicornButton = $("#addUnicornButton");
var killUnicornButton = $("#killUnicornButton");

// When the user clicks the button, open the modal
addUnicornButton.click(function() {
    addUnicornModal.css("display", "block");
});

killUnicornButton.click(function() {
    killUnicornModal.css("display", "block");
});

// Change button text on kill unicorn button hover
killUnicornButton.hover(
    function() {
        var $this = $(this); // cache initial text
        $this.data('initialText', $this.text());
        $this.text(":(");
    },
    function() {
        var $this = $(this); // caching $(this)
        $this.text($this.data('initialText'));
    }
);

// When the user clicks anywhere outside of the modal, close it
$(window).click(function(event) {
    if ($(event.target).is(addUnicornModal)) {
        console.log("Killing add modal");
        addUnicornModal.css("display", "none");
    }
    if ($(event.target).is(killUnicornModal)) {
        console.log("Killing kill modal");
        killUnicornModal.css("display", "none");
    }
});
/*
  END MODAL LOGIC
 */

/*
  START TOAST LOGIC
 */
function showToast(message) {

    var x = document.getElementById("toastBar");
    // Add the "show" class to DIV
    x.className = "show";
    x.innerHTML = message;

    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}
/*
  END TOAST LOGIC
 */