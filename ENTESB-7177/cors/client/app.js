$(document).ready(function() {
  $.get("http://localhost:3000/hello", function(data) {
    $("#result-hello").text(data);
  });
  $.get("http://localhost:3000/goodbye", function(data) {
    $("#result-goodbye").text(data);
  });
});
