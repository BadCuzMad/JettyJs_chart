async function go(){
var ctx = document.getElementById('myChart');
const chartData = await getFileData();
var myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: chartData.xs,
        datasets: [{
            label: '# of Votes',
            data: chartData.ys,
            backgroundColor: [
                'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
                'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1
        }]
    }
});
}



async function getFileData(){
    const response = await fetch ('./js/js_file_data/23.07.21table.txt');
    const xs = []
    const ys = []
    const data = await response.text();

    const table = data.split('\n').slice(1);
    table.forEach(row =>  {
    const columns = row.split(':');

    const xData = columns[7];
    xs.push(xData);
    const yData = columns[9];
    ys.push(yData);

    })
   return { xs, ys };
}
go();