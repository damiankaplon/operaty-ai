import {type TypedUseSelectorHook, useDispatch, useSelector} from 'react-redux';
import type {AppDispatch, AppState} from "./store.ts";

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<AppState> = useSelector;
