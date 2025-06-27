import {Button, ButtonGroup, Container, Paper, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";

export type OperatFamily = 'drogowy' | 'mostowy';

export default function OperatSelect() {
    const navigate = useNavigate()
    return (
        <div style={{width: '100vw', height: '100vh', backgroundColor: '#f5f5f5'}}>
            <Container sx={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 2, height: '100vh', minWidth: '300px'}}>
                <Paper elevation={3} sx={{p: 4, borderRadius: 2}}>
                    <Typography variant="h4" component="h1" sx={{mb: 2}}>
                        Wybierz rodzaj operatu
                    </Typography>
                    <ButtonGroup orientation="vertical" variant="text" size="large" fullWidth>
                        <Button onClick={() => navigate("road")} color="primary">Drogowy</Button>
                        <Button onClick={() => navigate("bridge")} color="secondary">Mostowy</Button>
                    </ButtonGroup>
                </Paper>
            </Container>
        </div>
    )
}
