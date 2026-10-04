import { useEffect, useMemo, useState } from 'react'
import type { ChangeEvent, FormEvent, ReactNode } from 'react'
import { Link, Route, Routes, useNavigate, useParams } from 'react-router-dom'
import { products } from './data/products'
import type { CartItem, Product } from './types'
import { userApi, type Address, type AddressInput, type User } from './api'

const money = (n: number) => `₹${n.toLocaleString('en-IN')}`

function App() {
  const [cart, setCart] = useState<CartItem[]>([])

  const addToCart = (product: Product) => {
    setCart(items => {
      const found = items.find(x => x.id === product.id)
      return found
        ? items.map(x => x.id === product.id ? { ...x, quantity: x.quantity + 1 } : x)
        : [...items, { ...product, quantity: 1 }]
    })
  }

  const updateQuantity = (id: number, delta: number) => {
    setCart(items => items
      .map(x => x.id === id ? { ...x, quantity: x.quantity + delta } : x)
      .filter(x => x.quantity > 0))
  }

  const [user, setUser] = useState<User | null>(null)
  useEffect(() => { if (localStorage.getItem('ecommerce_token')) userApi.me().then(r => setUser(r.data)).catch(() => { localStorage.removeItem('ecommerce_token'); localStorage.removeItem('ecommerce_user') }) }, [])
  const logout = () => { localStorage.removeItem('ecommerce_token'); localStorage.removeItem('ecommerce_user'); setUser(null); window.location.href = '/login' }

  return (
    <div className="app">
      <Header cartCount={cart.reduce((sum, x) => sum + x.quantity, 0)} user={user} onLogout={logout} />
      <Routes>
        <Route path="/" element={<Home onAdd={addToCart} />} />
        <Route path="/products" element={<Products onAdd={addToCart} />} />
        <Route path="/products/:id" element={<ProductDetails onAdd={addToCart} />} />
        <Route path="/cart" element={<Cart items={cart} updateQuantity={updateQuantity} />} />
        <Route path="/login" element={<Login onLogin={setUser} />} />
        <Route path="/register" element={<Register onRegister={setUser} />} />
        <Route path="/orders" element={<Orders />} />
        <Route path="/profile" element={<Profile user={user} onUserChange={setUser} />} />
      </Routes>
      <Footer />
    </div>
  )
}

function Header({ cartCount, user, onLogout }: { cartCount: number; user: User | null; onLogout: () => void }) {
  return (
    <header className="header">
      <div className="header-inner">
        <Link to="/" className="brand"><span>●</span> ShopSphere</Link>
        <nav className="nav">
          <Link to="/products">Shop</Link><Link to="/orders">Orders</Link>
          {user ? <><Link to="/profile">{user.firstName}</Link><button className="nav-button" onClick={onLogout}>Logout</button></> : <Link to="/login">Login</Link>}
          <Link to="/cart" className="cart-link">Cart <b>{cartCount}</b></Link>
        </nav>
      </div>
    </header>
  )
}

function Home({ onAdd }: { onAdd: (p: Product) => void }) {
  return (
    <main>
      <section className="hero">
        <div>
          <span className="eyebrow">PHASE 2 • E-COMMERCE UI</span>
          <h1>Everything you need.<br /><em>One simple shop.</em></h1>
          <p>Discover electronics, fashion and everyday essentials with a clean, modern shopping experience.</p>
          <Link className="primary-btn" to="/products">Shop products →</Link>
        </div>
        <div className="hero-card">
          <div className="hero-image" />
          <div className="floating-card">
            <span>★ 4.8</span>
            <strong>Top rated products</strong>
            <small>Thousands of happy customers</small>
          </div>
        </div>
      </section>

      <section className="section">
        <div className="section-head">
          <div><span className="eyebrow">CURATED FOR YOU</span><h2>Featured products</h2></div>
          <Link to="/products" className="text-link">View all →</Link>
        </div>
        <div className="product-grid">
          {products.slice(0, 4).map(p => <ProductCard key={p.id} product={p} onAdd={onAdd} />)}
        </div>
      </section>

      <section className="benefits">
        <Benefit icon="↗" title="Fast delivery" text="Reliable shipping to your doorstep." />
        <Benefit icon="✓" title="Secure payments" text="Payment integration ready for Phase 6." />
        <Benefit icon="↺" title="Easy returns" text="Simple customer-friendly return flow." />
        <Benefit icon="♡" title="Trusted quality" text="Curated products and ratings." />
      </section>
    </main>
  )
}

function Benefit({ icon, title, text }: { icon: string, title: string, text: string }) {
  return <div className="benefit"><span>{icon}</span><div><strong>{title}</strong><p>{text}</p></div></div>
}

function Products({ onAdd }: { onAdd: (p: Product) => void }) {
  const [query, setQuery] = useState('')
  const [category, setCategory] = useState('All')
  const categories = ['All', ...Array.from(new Set(products.map(p => p.category)))]
  const filtered = useMemo(() => products.filter(p =>
    (category === 'All' || p.category === category) &&
    p.name.toLowerCase().includes(query.toLowerCase())
  ), [query, category])

  return (
    <main className="container page">
      <div className="page-heading">
        <span className="eyebrow">PRODUCT CATALOG</span>
        <h1>Shop all products</h1>
        <p>Browse the catalog UI that will connect to Catalog and Search Services in later phases.</p>
      </div>
      <div className="filters">
        <input value={query} onChange={e => setQuery(e.target.value)} placeholder="Search products..." />
        <select value={category} onChange={e => setCategory(e.target.value)}>
          {categories.map(c => <option key={c}>{c}</option>)}
        </select>
      </div>
      <div className="product-grid">
        {filtered.map(p => <ProductCard key={p.id} product={p} onAdd={onAdd} />)}
      </div>
    </main>
  )
}

function ProductCard({ product, onAdd }: { product: Product, onAdd: (p: Product) => void }) {
  return (
    <article className="product-card">
      <Link to={`/products/${product.id}`} className="product-image-wrap">
        {product.badge && <span className="badge">{product.badge}</span>}
        <img src={product.image} alt={product.name} />
      </Link>
      <div className="product-info">
        <span className="category">{product.category}</span>
        <Link to={`/products/${product.id}`}><h3>{product.name}</h3></Link>
        <div className="rating">★ {product.rating} <span>({product.reviews})</span></div>
        <div className="price-row">
          <strong>{money(product.price)}</strong>
          {product.oldPrice && <del>{money(product.oldPrice)}</del>}
        </div>
        <button className="add-btn" onClick={() => onAdd(product)}>Add to cart</button>
      </div>
    </article>
  )
}

function ProductDetails({ onAdd }: { onAdd: (p: Product) => void }) {
  const { id } = useParams()
  const product = products.find(p => p.id === Number(id))
  if (!product) return <main className="container page"><h1>Product not found</h1></main>
  return (
    <main className="container page detail">
      <div className="detail-image"><img src={product.image} alt={product.name} /></div>
      <div className="detail-copy">
        <span className="eyebrow">{product.category}</span>
        <h1>{product.name}</h1>
        <div className="rating big">★ {product.rating} <span>{product.reviews} customer reviews</span></div>
        <div className="detail-price">{money(product.price)} {product.oldPrice && <del>{money(product.oldPrice)}</del>}</div>
        <p>Premium product with a clean shopping experience. Product specifications, inventory availability and live pricing will be supplied by backend services in later phases.</p>
        <button className="primary-btn" onClick={() => onAdd(product)}>Add to cart</button>
      </div>
    </main>
  )
}

function Cart({ items, updateQuantity }: { items: CartItem[], updateQuantity: (id: number, d: number) => void }) {
  const subtotal = items.reduce((s, x) => s + x.price * x.quantity, 0)
  return (
    <main className="container page">
      <div className="page-heading"><span className="eyebrow">SHOPPING CART</span><h1>Your cart</h1></div>
      {items.length === 0 ? (
        <div className="empty"><h2>Your cart is empty</h2><p>Add a few products to see them here.</p><Link className="primary-btn" to="/products">Continue shopping</Link></div>
      ) : (
        <div className="cart-layout">
          <div className="cart-list">
            {items.map(item => (
              <div className="cart-item" key={item.id}>
                <img src={item.image} alt="" />
                <div className="cart-main"><span className="category">{item.category}</span><h3>{item.name}</h3><strong>{money(item.price)}</strong></div>
                <div className="quantity"><button onClick={() => updateQuantity(item.id, -1)}>−</button><span>{item.quantity}</span><button onClick={() => updateQuantity(item.id, 1)}>+</button></div>
                <strong>{money(item.price * item.quantity)}</strong>
              </div>
            ))}
          </div>
          <aside className="summary"><h2>Order summary</h2><div><span>Subtotal</span><strong>{money(subtotal)}</strong></div><div><span>Shipping</span><strong>Free</strong></div><hr /><div className="total"><span>Total</span><strong>{money(subtotal)}</strong></div><button className="primary-btn full">Checkout</button><small>Payment integration will be connected in Phase 6.</small></aside>
        </div>
      )}
    </main>
  )
}

function Login({ onLogin }: { onLogin: (u: User) => void }) {
  const navigate = useNavigate()
  const [email,setEmail]=useState('')
  const [password,setPassword]=useState('')
  const [error,setError]=useState('')
  const [loading,setLoading]=useState(false)
  const submit=async(e:FormEvent)=>{e.preventDefault();setError('');setLoading(true);try{const r=await userApi.login({email,password});localStorage.setItem('ecommerce_token',r.data.token);localStorage.setItem('ecommerce_user',JSON.stringify(r.data.user));onLogin(r.data.user);navigate('/profile')}catch(err){setError(err instanceof Error?err.message:'Login failed')}finally{setLoading(false)}}
  return <AuthForm title="Welcome back" subtitle="Sign in to continue shopping." action={loading?'Signing in…':'Sign in'} footer="New customer?" linkText="Create an account" linkTo="/register" onSubmit={submit} error={error}>
    <input required type="email" value={email} onChange={e=>setEmail(e.target.value)} placeholder="Email address" />
    <input required minLength={8} type="password" value={password} onChange={e=>setPassword(e.target.value)} placeholder="Password" />
  </AuthForm>
}
function Register({ onRegister }: { onRegister: (u: User) => void }) {
  const navigate=useNavigate(); const [form,setForm]=useState({firstName:'',lastName:'',email:'',mobile:'',password:''}); const [error,setError]=useState(''); const [loading,setLoading]=useState(false)
  const set=(k:keyof typeof form)=>(e:ChangeEvent<HTMLInputElement>)=>setForm({...form,[k]:e.target.value})
  const submit=async(e:FormEvent)=>{e.preventDefault();setError('');setLoading(true);try{await userApi.register(form);const login=await userApi.login({email:form.email,password:form.password});localStorage.setItem('ecommerce_token',login.data.token);localStorage.setItem('ecommerce_user',JSON.stringify(login.data.user));onRegister(login.data.user);navigate('/profile')}catch(err){setError(err instanceof Error?err.message:'Registration failed')}finally{setLoading(false)}}
  return <AuthForm title="Create your account" subtitle="Join ShopSphere and start shopping." action={loading?'Creating…':'Create account'} footer="Already have an account?" linkText="Sign in" linkTo="/login" onSubmit={submit} error={error}>
    <div className="form-row"><input required value={form.firstName} onChange={set('firstName')} placeholder="First name" /><input required value={form.lastName} onChange={set('lastName')} placeholder="Last name" /></div>
    <input required type="email" value={form.email} onChange={set('email')} placeholder="Email address" />
    <input required pattern="[0-9]{10,15}" value={form.mobile} onChange={set('mobile')} placeholder="Mobile number" />
    <input required minLength={8} type="password" value={form.password} onChange={set('password')} placeholder="Password (8+ characters)" />
  </AuthForm>
}
function AuthForm({title,subtitle,action,footer,linkText,linkTo,onSubmit,error,children}:{title:string;subtitle:string;action:string;footer:string;linkText:string;linkTo:string;onSubmit:(e:FormEvent)=>void;error:string;children:ReactNode}) {
  return <main className="auth-page"><form className="auth-card" onSubmit={onSubmit}><span className="brand center"><span>●</span> ShopSphere</span><h1>{title}</h1><p>{subtitle}</p>{children}{error&&<div className="form-error">{error}</div>}<button disabled={action.includes('…')} className="primary-btn full">{action}</button><div className="auth-footer">{footer} <Link to={linkTo}>{linkText}</Link></div></form></main>
}

function Orders() {
  return <main className="container page"><div className="page-heading"><span className="eyebrow">ACCOUNT</span><h1>Orders</h1></div><div className="empty"><h2>No orders yet</h2><p>Your order history will be populated by the Order Service in a later phase.</p><Link className="primary-btn" to="/products">Start shopping</Link></div></main>
}
function Profile({ user, onUserChange }: { user: User | null; onUserChange: (u:User)=>void }) {
  const [profile,setProfile]=useState({firstName:user?.firstName||'',lastName:user?.lastName||'',mobile:user?.mobile||''}); const [addresses,setAddresses]=useState<Address[]>([]); const [editing,setEditing]=useState<Address|null>(null); const [message,setMessage]=useState(''); const [error,setError]=useState('')
  useEffect(()=>{if(user){setProfile({firstName:user.firstName,lastName:user.lastName,mobile:user.mobile});loadAddresses()}},[user?.id])
  const loadAddresses=()=>userApi.addresses().then(r=>setAddresses(r.data)).catch(e=>setError(e.message))
  if(!user) return <main className="container page"><div className="empty"><h2>Please sign in</h2><p>Your profile and addresses are protected.</p><Link className="primary-btn" to="/login">Sign in</Link></div></main>
  const saveProfile=async(e:FormEvent)=>{e.preventDefault();setMessage('');setError('');try{const r=await userApi.updateProfile(profile);onUserChange(r.data);localStorage.setItem('ecommerce_user',JSON.stringify(r.data));setMessage('Profile updated successfully.')}catch(e){setError(e instanceof Error?e.message:'Update failed')}}
  return <main className="container page"><div className="page-heading"><span className="eyebrow">ACCOUNT</span><h1>My profile</h1><p>Real user data from the Phase 3 User Service.</p></div>
    <div className="profile-layout"><section className="profile-card"><div className="profile-head"><div className="avatar">{user.firstName[0]}</div><div><h2>{user.firstName} {user.lastName}</h2><p>{user.email} • {user.role}</p></div></div><form className="profile-form" onSubmit={saveProfile}><div className="form-row"><label>First name<input required value={profile.firstName} onChange={e=>setProfile({...profile,firstName:e.target.value})}/></label><label>Last name<input required value={profile.lastName} onChange={e=>setProfile({...profile,lastName:e.target.value})}/></label></div><label>Mobile<input required pattern="[0-9]{10,15}" value={profile.mobile} onChange={e=>setProfile({...profile,mobile:e.target.value})}/></label><button className="primary-btn">Save profile</button>{message&&<div className="form-success">{message}</div>}{error&&<div className="form-error">{error}</div>}</form></section>
    <AddressManager addresses={addresses} reload={loadAddresses} editing={editing} setEditing={setEditing}/></div></main>
}
function AddressManager({addresses,reload,editing,setEditing}:{addresses:Address[];reload:()=>void;editing:Address|null;setEditing:(a:Address|null)=>void}){
 const blank:AddressInput={addressType:'HOME',fullName:'',mobile:'',addressLine1:'',addressLine2:'',city:'',state:'',country:'India',postalCode:'',defaultAddress:addresses.length===0}; const [form,setForm]=useState<AddressInput>(blank); const [error,setError]=useState('');
 useEffect(()=>{if(editing)setForm({addressType:editing.addressType,fullName:editing.fullName,mobile:editing.mobile,addressLine1:editing.addressLine1,addressLine2:editing.addressLine2||'',city:editing.city,state:editing.state,country:editing.country,postalCode:editing.postalCode,defaultAddress:editing.defaultAddress});else setForm({...blank,defaultAddress:addresses.length===0})},[editing?.id,addresses.length])
 const submit=async(e:FormEvent)=>{e.preventDefault();setError('');try{if(editing)await userApi.updateAddress(editing.id,form);else await userApi.addAddress(form);setEditing(null);reload()}catch(e){setError(e instanceof Error?e.message:'Address save failed')}}
 const field=(key:keyof AddressInput)=>(e:ChangeEvent<HTMLInputElement>)=>setForm({...form,[key]:e.target.value})
 return <section className="profile-card address-section"><div className="section-head"><div><span className="eyebrow">ADDRESSES</span><h2>Delivery addresses</h2></div>{editing&&<button className="add-btn" type="button" onClick={()=>setEditing(null)}>Cancel edit</button>}</div>{addresses.length===0&&!editing&&<p className="muted">No saved addresses yet.</p>}<div className="address-list">{addresses.map(a=><div className="address-item" key={a.id}><div><strong>{a.fullName}</strong>{a.defaultAddress&&<span className="default-badge">Default</span>}<p>{a.addressLine1}{a.addressLine2&&`, ${a.addressLine2}`}<br/>{a.city}, {a.state} {a.postalCode}<br/>{a.country} • {a.mobile}</p></div><div className="address-actions"><button onClick={()=>setEditing(a)}>Edit</button>{!a.defaultAddress&&<button onClick={()=>userApi.setDefaultAddress(a.id).then(reload)}>Set default</button>}<button onClick={()=>userApi.deleteAddress(a.id).then(reload)}>Delete</button></div></div>)}</div><form className="address-form" onSubmit={submit}><h3>{editing?'Edit address':'Add address'}</h3><div className="form-row"><select value={form.addressType} onChange={e=>setForm({...form,addressType:e.target.value as AddressInput['addressType']})}><option>HOME</option><option>WORK</option><option>OTHER</option></select><input required value={form.fullName} onChange={field('fullName')} placeholder="Full name"/></div><div className="form-row"><input required pattern="[0-9]{10,15}" value={form.mobile} onChange={field('mobile')} placeholder="Mobile"/><input required value={form.postalCode} onChange={field('postalCode')} placeholder="Postal code"/></div><input required value={form.addressLine1} onChange={field('addressLine1')} placeholder="Address line 1"/><input value={form.addressLine2} onChange={field('addressLine2')} placeholder="Address line 2 (optional)"/><div className="form-row"><input required value={form.city} onChange={field('city')} placeholder="City"/><input required value={form.state} onChange={field('state')} placeholder="State"/></div><input required value={form.country} onChange={field('country')} placeholder="Country"/><label className="check"><input type="checkbox" checked={form.defaultAddress} onChange={e=>setForm({...form,defaultAddress:e.target.checked})}/> Make this my default address</label>{error&&<div className="form-error">{error}</div>}<button className="primary-btn">{editing?'Update address':'Add address'}</button></form></section>
}

function Footer() {
  return <footer><div className="footer-inner"><div><Link className="brand" to="/"><span>●</span> ShopSphere</Link><p>Phase 3 React + User Service integration.</p></div><div><strong>Platform</strong><p>Catalog • Cart • Orders • Account</p></div><div><strong>Roadmap</strong><p>Phase 3: User Service<br />Phase 4: Catalog & Search</p></div></div></footer>
}

export default App