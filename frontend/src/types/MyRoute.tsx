import {MyCoords} from "./MyCoords.tsx";
import {MyUsersDto} from "./MyUsersDto.tsx";

export type MyRoute = {
    id: string;
    coords: MyCoords[];
    members: MyUsersDto[];
    name: string;
    dateTime: Date
}