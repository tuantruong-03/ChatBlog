import { useEffect } from "react";
import { useAuth } from "../../hooks/auth-provider"


// For more understant about Context Provider
const TestContext: React.FC = () => {
    const auth = useAuth();
    let i = 0;
    useEffect(() => {
        i++;
        console.log(auth)
        console.log("auth change time ", i)
    }, [auth])

    return(<></>)
}
export default TestContext