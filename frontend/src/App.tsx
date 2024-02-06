
import './App.css'
import axios, {AxiosResponse} from "axios";
import useSWR, {SWRResponse} from "swr";
import {Route} from "./types/Route.tsx";





function App() {
  const {data, error}: SWRResponse<Route[], Error> = useSWR('/api/routes', async () => {
    const response:AxiosResponse<Route[], Error> = await axios.get('/api/routes');
    return response.data;
  });
  if (error) return <div>Error loading data</div>;
  if (!data) return <div>Loading data...</div>;
  return (
      <>
        <h1>Data from the backend:</h1>
        <ul>
          {data.map(item => (
              <li key={item.id}>{item.name}</li>
          ))}
        </ul>
      </>
  )
}

export default App
