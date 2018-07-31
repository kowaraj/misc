// URL: https://beta.observablehq.com/@kowaraj/bills
// Title: Bookkeeping of a shared appartment
// Author: kowaraj (@kowaraj)
// Version: 1271
// Runtime version: 1

const m0 = {
  id: "ef29a3386956098e@1271",
  variables: [
    {
      inputs: ["md"],
      value: (function(md){return(
md`# Bookkeeping of a shared appartment`
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`Pick the dates to account. Default is from the day Nicola arrived up to today.`
)})
    },
    {
      name: "dates",
      inputs: ["d3"],
      value: (function(d3){return(
{from:"2017-06-01", to: d3.timeFormat("%Y-%m-%d")(new Date())}
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`# Accounts`
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`Accounts show the balance and the debts`
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md` ### Nicola's account: `
)})
    },
    {
      inputs: ["debts","roundDebts"],
      value: (function(debts,roundDebts){return(
Object.assign({balance: debts[2].balance.toFixed(2)},  roundDebts(debts[2].debts))
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md` ### Irene's account: `
)})
    },
    {
      inputs: ["debts","roundDebts"],
      value: (function(debts,roundDebts){return(
Object.assign({balance: debts[0].balance.toFixed(2)},  roundDebts(debts[0].debts))
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md` ### Andrey's account: `
)})
    },
    {
      inputs: ["debts","roundDebts"],
      value: (function(debts,roundDebts){return(
Object.assign({balance: debts[1].balance.toFixed(2)},  roundDebts(debts[1].debts))
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`# Bills`
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`Selected bills (see the chosen dates above)`
)})
    },
    {
      name: "selectedbills",
      inputs: ["bills","d3","dates"],
      value: (function(bills,d3,dates){return(
bills.filter(x => {
  const dateBill = new Date(d3.timeParse("%Y-%m-%d")(x.date));
  const dateFrom = new Date(d3.timeParse("%Y-%m-%d")(dates.from));
  const dateTo = new Date(d3.timeParse("%Y-%m-%d")(dates.to));
  return (dateBill > dateFrom) && (dateBill < dateTo)})
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`# Transactions`
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`Who paid whom and what for. For the first three the date is 'fake', Nov 11 2017`
)})
    },
    {
      name: "transactions",
      value: (function(){return(
[
   {date: "2017-11-11", value: 61.78, description: "for edf", from: "Irene", to: "Andrey"},
   {date: "2017-11-11", value: 49.47, description: "for edf", from: "Nicola", to: "Irene"},
   {date: "2017-11-11", value: 58.11, description: "for edf", from: "Nicola", to: "Andrey"}
  ]
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`Let it be the day when we were even, Nov 11 2017. This would be the list of Transactions.`
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`# The rest of the code`
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`List of all the bills paid since 01.11.2017 (Andrey arrived)`
)})
    },
    {
      name: "bills",
      value: (function(){return(
[
       {date: "2017-05-28", value: 247.65, people: 2, type: "edf", paidby: "Andrey", note: "-May 2017"},
       {date: "2017-07-30", value: 64.6,   people: 2, type: "internet", paidby: "Irene", note: "Jun-Jul 2017"},
       {date: "2017-08-16", value: 172.24, people: 2, type: "edf", paidby: "Andrey", note: "May-Aug 2017"},
       {date: "2017-09-30", value: 150.00,  people: 3, type: "internet", paidby: "Irene", note: "Aug-Oct 2017"},
       {date: "2017-09-26", value: 174.33, people: 3, type: "edf", paidby: "Andrey", note: "Aug-Sep 2017"},
       {date: "2017-11-26", value: 330.55, people: 3, type: "edf", paidby: "Andrey", note: "Sep-Nov 2017"},
       {date: "2018-02-06", value: 109.21, people: 3, type: "edf", paidby: "Andrey", note: "Nov-Feb 2018"},
       {date: "2018-03-27", value: 391.25, people: 3, type: "edf", paidby: "Andrey", note: "Feb-Mar 2018"},
       {date: "2018-05-27", value: 193.47, people: 3, type: "edf", paidby: "Andrey", note: "Mar-May 2018"}
      ]
)})
    },
    {
      name: "inhabitants",
      value: (function(){return(
[ 
  //{date: "2000-01-01", people: [], 
  //                     note: "Fake First date"},
  {date: "2016-11-01", people: [{room: 2, name:"Irene"}, {room: 3, name:"Andrey"}] },
  {date: "2017-09-01", people: [{room: 1, name:"Nicola"}, {room: 2, name:"Irene"}, {room: 3, name:"Andrey"} ], 
                       note: "Nicola arrived"}//,
  //{date: "2999-01-01", people: [],
  //                     note: "Fake Last date"}
  ]
)})
    },
    {
      name: "accounts",
      inputs: ["people","getPeopleButOneAsObject","selectedbills","processBill"],
      value: (function(people,getPeopleButOneAsObject,selectedbills,processBill)
{
  console.log("recalc accounts...")
  const accs0 = [];  
  people.forEach(x => accs0.push({name:x, balance:0, debts: getPeopleButOneAsObject(x), owes:[]}));
  const temp = accs0.map(a => ({...a}));
  const ret = selectedbills.reduce( (acc,cur) => processBill(acc, cur), temp)
  ret.forEach(x=>Object.keys(x.debts).forEach(k=>console.log("debts[k] = ", x.debts[k])))
  console.log("end")
  return ret;
}
)
    },
    {
      name: "debts",
      inputs: ["people","accounts","transactions"],
      value: (function(people,accounts,transactions)
{
  console.log("recalc debts...")

  const innerloop = (name) => {
  return Array.from(people).filter(p => p != name).reduce (
                     (acc,cur) => {return Object.assign( { [cur] : 0 }, acc )},
                     new Object())
  }

  const accs0 = accounts.map(x => ({...x}))
  accs0.map(a => a.debts = innerloop(a.name))
  
  // adding up the debts to the same person
  const accs1 = accs0.map(x => ({...x}))  
  accs1.forEach(a => {    a.owes.forEach(o => { a.debts[o.whom] += o.amount; })     })

  // substructing debts of the opposite
  const accs2 = accounts.map(x => ({...x}))
  accs2.forEach(a2 => {    
    Object.keys(a2.debts).forEach(dk => { 
      const b2a = accs1.find(a=>a.name==dk).debts[a2.name]
      const a2b = accs1.find(a=>a.name==a2.name).debts[dk]
      a2.debts[dk] = a2b - b2a
    }) 
  })
  
  //process Transactions for 'balance'
  transactions.forEach(tr => {
    console.log("tr = ", tr)
    
    const bFrom = accs2.find(a=>a.name==tr.from).balance
    console.log("bFrom2 = ", bFrom)
    const bTo = accs2.find(a=>a.name==tr.to).balance
    console.log("bTo2 = ", bTo)
    
    accs2.find(a=>a.name==tr.from).balance = bFrom - tr.value
    console.log("bFrom' = ", accs2.find(a=>a.name==tr.from).balance)
    accs2.find(a=>a.name==tr.to).balance = bTo + tr.value
    console.log("bTo' = ", accs2.find(a=>a.name==tr.to).balance)
    
    const debTo = accs2.find(a=>a.name==tr.from).debts[tr.to] 
    const debFrom = accs2.find(a=>a.name==tr.to).debts[tr.from]
    
    accs2.find(a=>a.name==tr.from).debts[tr.to] = debTo - tr.value
    console.log("debTo' = ", accs2.find(a=>a.name==tr.from).debts[tr.to])
    accs2.find(a=>a.name==tr.to).debts[tr.from] = debFrom  + tr.value
    console.log("debTo' = ", accs2.find(a=>a.name==tr.to).debts[tr.from])
    
  })
  /*
    //process Transactions for 'debts'
  const accs3 = accounts.map(x => ({...x}))  
  transactions.forEach(tr => {
    console.log("tr = ", tr)    
    
    accs3.find(a=>a.name==tr.from).debts[tr.to] = accs2.find(a=>a.name==tr.from).debts[tr.to] - tr.value
    accs3.find(a=>a.name==tr.to).debts[tr.from] = accs2.find(a=>a.name==tr.to).debts[tr.from] + tr.value
  })*/
  return accs2
}
)
    },
    {
      name: "processBill",
      inputs: ["getPeopleFromBill"],
      value: (function(getPeopleFromBill){return(
(accounts_, bill) => {
  //console.log("bills = ", bill)
  const people = getPeopleFromBill(bill)
  accounts_.forEach(a => {
    //console.log("a = ", a)
    if (a.name == bill.paidby) {
      a.balance -= bill.value;
    } else if (people.includes(a.name)) {
      a.owes.push( {whom: bill.paidby, amount: bill.value / people.length, for: bill.type} );
      //a.debts
    } else {
      //console.log("account not affected:", a.name, people);
    }
  })
  return accounts_;
}
)})
    },
    {
      name: "people",
      inputs: ["inhabitants"],
      value: (function(inhabitants){return(
inhabitants.reduce( (acc, cur) => { 
    return new Set(Array.from(acc).concat(cur.people.map(x=>x.name) ))
  }, [])
)})
    },
    {
      name: "getPeopleFromBill",
      inputs: ["inhabitants"],
      value: (function(inhabitants){return(
(bill) => { 
  const idx = inhabitants.sort((x,y)=>(x.date < y.date)).findIndex(i => i.date <= bill.date)
  return inhabitants[idx].people.map(p=>p.name);
}
)})
    },
    {
      name: "getPeopleButOneAsObject",
      inputs: ["people"],
      value: (function(people){return(
(name) => {
  return Array.from(people).filter(p => p!=name).reduce( 
    (acc,cur) => {
      return Object.assign( {[cur]: 0}, acc)},
    new Object)
}
)})
    },
    {
      name: "roundDebts",
      value: (function(){return(
(d) => {
  const x = new Object();
  Object.keys(d).forEach(k => x[k] = d[k].toFixed(2))
  return x;
}
)})
    },
    {
      name: "d3",
      inputs: ["require"],
      value: (function(require){return(
require("https://d3js.org/d3.v5.min.js")
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`## ToDo`
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`Fix *getPeopleFromBill()*`
)})
    },
    {
      inputs: ["getPeopleFromBill"],
      value: (function(getPeopleFromBill){return(
getPeopleFromBill({
  date: "2017-09-02",
  value: 247.65,
  people: 2,
  type: "edf",
  paidby: "Andrey",
  note: "-May 2017",
}).length == 3
)})
    },
    {
      name: "display_debts",
      inputs: ["debts","roundDebts"],
      value: (function(debts,roundDebts)
{
  console.log("recalc display_debts...")
  return debts.map(d => (Object.assign(  { name: d.name}, roundDebts(d.debts)) )) 
}
)
    }
  ]
};

const notebook = {
  id: "ef29a3386956098e@1271",
  modules: [m0]
};

export default notebook;
