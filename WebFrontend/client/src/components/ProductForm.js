import React , {useState} from 'react'
import 'materialize-css';
import {TextInput, Button} from 'react-materialize'
import './form.css'
import Axios from 'axios'
export default function ProductForm() {

    const [state, setstate] = useState({})
    const handleChange=(e)=>{
        const {name , value} = e.target
        setstate(prev=>({...prev,[name]:value}))
       
    }
    const sendData =async ()=>{
      
        await fetch(`http://localhost:5000/products`, {
            mode: "cors",
            headers: { "Content-Type": "application/json" },
            method: "POST",
            body:JSON.stringify(state),
          })
            .then((res) => console.log(res))

    }
    return (
        <div className="container center_div">

        <div className="row">
        
        <div className="col text-center">
        <TextInput 
        type="date" 
        placeholder="Date"
        name="date"
        inputClassName="form-control  mb-3"
        onChange={handleChange}
        value={state.date}
        />
        </div>
        </div>
        <div className="row">
        
        <div className="col text-center">
            <TextInput 
            inputClassName="form-control  mb-3"
            placeholder="Region"
            name="region"
            onChange={handleChange}
            value={state.region}
            />
            </div>
            </div>
            <div className="row">
        
            <div className="col text-center">
            <TextInput
            inputClassName="form-control  mb-3"
            placeholder="Product"
            name="product"
            onChange={handleChange}
            value={state.product}
            />
            </div>
            </div>
            <div className="row">
        
            <div className="col text-center">
            <TextInput 
            type="number"
            inputClassName="form-control  mb-3"
            placeholder="Quantity"
            name="qty"
            onChange={handleChange}
            value={state.qty}
            />
            </div>
            </div>
            <div className="row">
        
            <div className="col text-center">
            <TextInput 
              type="number"
            inputClassName="form-control  mb-3"
            placeholder="Cost"
            name="cost"
            onChange={handleChange}
            value={state.cost}
            />
            </div>
            </div>
            
            <div className="row">
        
            <div className="col text-center">
            <TextInput
              type="number"
            inputClassName="form-control  mb-3"
            placeholder="AMT"
            name="amt"
            onChange={handleChange}
            value={state.amt}
            />
            </div>
            </div>
           
            <div className="row">
        
            <div className="col text-center">
            <TextInput 
            type="number"
          inputClassName="form-control  mb-3"
          placeholder="Tax"
          name="tax"
          onChange={handleChange}
          value={state.tax}
          />
            </div>
            </div>
            
            <div className="row">
        
            <div className="col text-center">
            <TextInput 
              type="number"
            inputClassName="form-control  mb-3"
            placeholder="Total"
            name="total"
            onChange={handleChange}
            value={state.total}
            />
            </div>
            </div>
        
            <div className="row">
        
            <div className="col text-center">
            <Button className="btn btn-default btn-primary" onClick={sendData} >Submit</Button>
            </div>
            </div>
            
        </div>
    )
}
