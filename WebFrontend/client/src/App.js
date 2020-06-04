import 'materialize-css';
import {Container ,Navbar , NavItem} from 'react-materialize'
import React from 'react';
import logo from './logo.svg';
import './App.css'
import {BrowserRouter as Router,Route,Link,Switch} from 'react-router-dom'
import Products from './components/Products';
import ProductForm from './components/ProductForm';
function App() {
  return (
    <div>
   
    <div className="container pb-3">
    <div className="title">Rabbit MQ distributed server</div>
    
    
    
    <Router>
    <div>
    <div className="navbar">
    <ul className="nav-items">
    <li>
    <Link to="/" className="nav-item">Products</Link>
    </li>
    <li>
    <Link to="/addProduct" className="nav-item">Add Product</Link>
    </li>
  </ul>
  </div>  
      <hr />

      {/*
        A <Switch> looks through all its children <Route>
        elements and renders the first one whose path
        matches the current URL. Use a <Switch> any time
        you have multiple routes, but you want only one
        of them to render at a time
      */}
      <Switch>
        <Route exact path="/">
          <Products />
        </Route>
        <Route path="/addProduct">
          <ProductForm />
        </Route>
      </Switch>
    </div>
  </Router>

    </div>
    </div>
    

  );
}

export default App;
