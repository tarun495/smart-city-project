// Initialize map centered on Mumbai (change coords to your city)
var map = L.map('map').setView([19.0760, 72.8777], 13);

// OpenStreetMap tile layer (free, no API key needed)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);

// Custom red marker icon
var redIcon = L.icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34]
});

// Load business locations from server
fetch('/map/api/locations')
    .then(response => response.json())
    .then(locations => {
        locations.forEach(function(loc) {
            if (loc.latitude && loc.longitude) {
                L.marker([loc.latitude, loc.longitude], {icon: redIcon})
                    .addTo(map)
                    .bindPopup(
                        '<b>' + loc.name + '</b><br>' +
                        loc.category + '<br>' +
                        loc.address + '<br>' +
                        '<small>' + (loc.phone || '') + '</small>'
                    );
            }
        });
    })
    .catch(err => console.log('Map data error:', err));