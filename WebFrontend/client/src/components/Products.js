import React, { useEffect , useState } from 'react'

export default function Products() {


    const [Products, setProducts] = useState([])
    const getData = async ()=>{
        await fetch(`http://localhost:5000/products`, {
           
            mode: "cors",
          })
            .then((res) => res.json())
            .then((docs) => (setProducts(docs)));

    }
    useEffect(() => {
        getData();
   
       
    }, [])
    return (
        <div className="mb-4">
        <table className="table table-dark mb-4">
        <tr>
        <th>
        id
        </th>
        <th>
             Product name
        </th>
        </tr>
       { Products && ( 
       Products.map(row=>(
           <tr>
           <td>{row.id}</td>
           <td>{row.product}</td>
           </tr>
       ))
       
    )

     
        }
        </table>
        </div>
    )
}
