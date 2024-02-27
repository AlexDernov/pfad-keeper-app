import RouteForm from "./route-form.tsx";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import {MyUsersDto} from "../types/MyUsersDto.tsx";


type PropsNewRoute = {
    onSubmit: (route: MyRouteDto) => void;
    logInUser:MyUsersDto;
}
export default function NewRoute(props: Readonly<PropsNewRoute>) {
    return (
            <RouteForm onSubmit={props.onSubmit} name={""} routeData={undefined} date={""} isEdit={false} coords={[]} logInUser={props.logInUser} routeId={""} onDeleteMembers={() => {}} usersOfRoute={[]} allUsers={[]}/>
    )
}
