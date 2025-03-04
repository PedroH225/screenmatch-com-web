package br.com.alura.screenmatchweb.service;

public interface IConverteDados {
    <T> T  obterDados(String json, Class<T> classe);
}
