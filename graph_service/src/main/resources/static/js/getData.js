const {Client} = require('pg');
const config = require("./dbConfig.js")
const client = new Client(config)


async function getYData(){
   let arry = [];
   let yvalues = [];
   const client =  await new Client(config);
   await client.connect();
   var resy = await client.query('SELECT koef_shuma FROM signal')
   yvalues = resy.rows;
   for(let i = 0; i < yvalues.length;i++){
      arry[i] = yvalues[i].koef_shuma;
   }
   await client.end();
   return arry;
}

async function getXData(){
   let xvalues = [];
   let arrx = [];
   const client =  await new Client(config);
   await client.connect();
   var res = await client.query('SELECT decibel FROM signal')
   xvalues = res.rows;
      for(let i = 0; i < xvalues.length;i++){
         arrx[i] = xvalues[i].decibel;
      }
   await client.end();
   return arrx;
}

module.exports = {
    getXData,
    getYData
};
