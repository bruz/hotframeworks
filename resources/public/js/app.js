var Hotframeworks = {

  graph: function(data) {
    var palette = new Rickshaw.Color.Palette();

    var seriesMaxes = [],
        seriesMins = [];
    $.each(data, function(index, framework) {
      framework.color = palette.color();
      seriesMaxes.push( Math.max.apply(Math, framework.data) );
      seriesMins.push( Math.min.apply(Math, framework.data) );
    });

    var max = Math.max.apply(Math, seriesMaxes);
    var min = Math.min.apply(Math, seriesMins);

    var graph = new Rickshaw.Graph( {
      element: document.querySelector("#graph"),
      renderer: 'line',
      stroke: true,
      series: data,
      height: 500,
      min: min,
      max: max
    });

    var legend = new Rickshaw.Graph.Legend({
      graph: graph,
      element: document.querySelector("#legend"),
      naturalOrder: true
    });

    var shelving = new Rickshaw.Graph.Behavior.Series.Toggle({
      graph: graph,
      legend: legend
    });

    var hoverDetail = new Rickshaw.Graph.HoverDetail( {
      graph: graph
    } );

    graph.render();

    var time = new Rickshaw.Fixtures.Time();
    var timeUnit = time.unit('week');
    var xAxis = new Rickshaw.Graph.Axis.Time( {
      graph: graph,
      timeUnit: timeUnit,
      timeFixture: new Rickshaw.Fixtures.Time.Local()
    });

    /*
    var xAxis = new Rickshaw.Graph.Axis.X({
      graph: graph,
      tickFormat: function(value) {
        var date = new Date(value * 1000);
        return date.getMonth() + '/' + date.getDate() + '/' + date.getYear();
      }
    });
    */

    xAxis.render();

    var yAxis = new Rickshaw.Graph.Axis.Y({
      graph: graph
    });

    yAxis.render();
  }

};
