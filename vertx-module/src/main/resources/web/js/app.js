var canvas = document.getElementById('canvas');
var context = canvas.getContext('2d');
var eventBus = new EventBus('http://localhost:8080/eventbus');

/**
 * Draw whatever is required by the server.
 */

eventBus.onopen = function () {
    eventBus.registerHandler('draw.points', function (error, message) {
        if (!error) {
            var point = message.body;

            context.beginPath();
            context.arc(point.x, point.y, point.radius, 0, 2 * Math.PI, false);
            context.fillStyle = point.color;
            context.fill();

        }
    });
};


/**
 * Handle mouse events and send data to the server.
 */
var currentDrawing = 0;
canvas.addEventListener('mouseup', function(evt) {
    currentDrawing++;
});

canvas.addEventListener('mousemove', function(evt) {
    if (evt.which == 1) { // left button pressed
        eventBus.send("draw.positions", getMousePos(canvas, evt));
    }
}, false);

function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
        drawing: currentDrawing,
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}


