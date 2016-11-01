COLORS = ['#a6cee3','#1f78b4','#b2df8a','#33a02c','#fb9a99','#e31a1c','#fdbf6f','#ff7f00','#cab2d6','#6a3d9a'];

var Hotframeworks = {

  graph: function(data) {
    var ctx = document.getElementById('graph')
    new Chart(ctx, {
      type: 'line',
      data: {
        datasets: data.map(function(framework, index) {
          return {
            label: framework.name,
            data: framework.data.map(function(point) {
              return {
                x: new Date(point.x * 1000),
                y: point.y
              }
            }),
            borderColor: COLORS[index],
            backgroundColor: COLORS[index],
            fill: false
          }
        })
      },
      options: {
        maintainAspectRatio: false,
        scales: {
          xAxes: [{
            type: 'time',
            position: 'bottom'
          }]
        },
        tooltips: {
          callbacks: {
            title: function(tooltipItem) {
              return moment(tooltipItem[0].xLabel).format('LL');
            }
          }
        }
      }
    });
  }
};
