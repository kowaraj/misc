// URL: https://beta.observablehq.com/@kowaraj/bills
// Title: Bookkeeping of a shared appartment\n\n *Version 1.2 @20180801. Comments, suggestions are very welcome!*
// Author: kowaraj (@kowaraj)
// Version: 1572
// Runtime version: 1

const m0 = {
  id: "ef29a3386956098e@1572",
  variables: [
    {
      inputs: ["md"],
      value: (function(md){return(
md`# Bookkeeping of a shared appartment\n\n *Version 1.2 @20180801. Comments, suggestions are very welcome!*`
)})
    },
    {
      inputs: ["md","accounts","roundDebts","debts"],
      value: (function(md,accounts,roundDebts,debts){return(
md`## Summary\n\n
${
Object.keys(accounts).map(a => {
  
return "\n\n- #### " + accounts[a].name + " owes to \n" 
  + Object.keys(roundDebts(debts[a].debts)).map(
    k => {
      const x = roundDebts(debts[a].debts)[k]
      if (x > 1)
        return ""+k+" : "+x
      else
        return ""+k+" : --" 
    })
    .map(x => " - *"+x+"*").join("\n\n")
})                   
}
`
)})
    },
    {
      inputs: ["md"],
      value: (function(md){return(
md`### Dates \n\n *We start from 2017.06.01. The day that the first transactions (calculated by Irene) accounted for (and it brakes if you try to start from the first day of Nicola's arrival).*`
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
md`\n---\n\n*The rest just code...*\n\n---\n\n# Accounts\n\n*Accounts show the balance and the debts*`
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
md`# Bills\n\n*Selected bills (see the chosen dates above)*`
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
md`# Transactions\n\n*Who paid whom, how much and what for... ;)*`
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
       {date: "2017-07-30", value: 172.24, people: 2, type: "edf", paidby: "Andrey", note: "May-Aug 2017"},
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
  {date: "2017-08-01", people: [{room: 1, name:"Nicola"}, {room: 2, name:"Irene"}, {room: 3, name:"Andrey"} ], 
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
  //temp.forEach(a => console.log("temp accounts: "+ a.name + ", " + a.balance + ", D= ", a))

  const ret = selectedbills.reduce( (acc,cur) => processBill(acc, cur), temp)
  ret.forEach(x=>Object.keys(x.debts).forEach(k=>console.log("debts[k] = ", x.debts[k])))
  console.log("end")
  return ret;
}
)
    },
    {
      name: "debts",
      inputs: ["accounts","people","transactions"],
      value: (function(accounts,people,transactions)
{
  console.log("\nrecalc debts...")
  accounts.forEach(a => console.log("account: "+ a.name + ", " + a.balance + " = ", a.debts))
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
  console.log("opposite debts substruction...")
  const accs2 = accounts.map(x => ({...x}))
  accs2.forEach(a2 => {    
    Object.keys(a2.debts).forEach(dk => { 
      const b2a = accs1.find(a=>a.name==dk).debts[a2.name]
      const a2b = accs1.find(a=>a.name==a2.name).debts[dk]
      a2.debts[dk] = a2b - b2a
    }) 
  })
  
  //process Transactions for 'balance'
  console.log("Transactions...")
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
      inputs: ["getPeopleFromBill","inhabitants"],
      value: (function(getPeopleFromBill,inhabitants){return(
(accounts_, bill) => {
  console.log("\nbills = ", bill)
  const people = getPeopleFromBill(bill, inhabitants)
  accounts_.forEach(a => {
    console.log("a = ", a)
    if (a.name == bill.paidby) {
      console.log("payer")
      a.balance -= bill.value;
    } else if (people.includes(a.name)) {
      console.log("debitor")
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
      value: (function(){return(
(bill, inh_) => { 
  const idx = inh_.sort((x,y)=>(x.date < y.date)).findIndex(i => i.date <= bill.date)
  const pp = inh_[idx].people.map(p=>p.name)
  //console.log("people from bill = ", pp)
  return pp;
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
      inputs: ["md"],
      value: (function(md){return(
md`# Some unit tests...`
)})
    },
    {
      inputs: ["getPeopleFromBill","inhabitants"],
      value: (function(getPeopleFromBill,inhabitants){return(
getPeopleFromBill({
  date: "2017-09-02",
  value: 247.65,
  people: 2,
  type: "edf",
  paidby: "Andrey",
  note: "-May 2017",
}, inhabitants).length == 3
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
    },
    {
      name: "test_01_inhabitants",
      value: (function(){return(
[ 
  //{date: "2000-01-01", people: [], 
  //                     note: "Fake First date"},
  {date: "2016-11-01", people: [{room: 2, name:"Irene"}, {room: 3, name:"Andrey"}] },
  {date: "2017-08-01", people: [{room: 1, name:"Nicola"}, {room: 2, name:"Irene"}, {room: 3, name:"Andrey"} ], 
                       note: "Nicola arrived"}//,
  //{date: "2999-01-01", people: [],
  //                     note: "Fake Last date"}
  ]
)})
    },
    {
      name: "test_01_bills",
      value: (function(){return(
[   {date: "2017-07-30", value: 172.24, people: 2, type: "edf", paidby: "Andrey", note: "May-Aug 2017"}
                 ]
)})
    },
    {
      name: "RESULT_OF__test_01",
      inputs: ["test_01_bills","getPeopleFromBill","test_01_inhabitants"],
      value: (function(test_01_bills,getPeopleFromBill,test_01_inhabitants){return(
test_01_bills.map(b => getPeopleFromBill(b, test_01_inhabitants))[0].length == 2
)})
    }
  ]
};

const notebook = {
  id: "ef29a3386956098e@1572",
  modules: [m0]
};

export default notebook;
