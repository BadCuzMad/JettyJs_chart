const {Client} = require('pg');
const config = require("./config.js")
const client = new Client(config)


async function getYData(){
   let arry = [];
   let yvalues = [];
   const client =  await new Client(config);
   await client.connect();
   var res = await client.query('SELECT koef_shuma FROM signal')
   yvalues = res.rows;
   for(let i = 0; i < yvalues.length;i++){
      arry[i] = yvalues[i].koef_shuma;
   }
   await client.end();
   return arry[0];
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
   return arrx[0];
}


module.exports = {
    getXData,
    getYData
};
