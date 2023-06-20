package model;

public class entrada {

    public int idestoque;
    public int idtiposervico;
    public String codigo;
    public String data;
    public Double preco;
    public String detalhes;
    public int quantidade;
    public int formapagamento;

    public int getIdestoque() {
        return idestoque;
    }

    public void setIdestoque(int idestoque) {
        this.idestoque = idestoque;
    }

    public int getIdtiposervico() {
        return idtiposervico;
    }

    public void setIdtiposervico(int idtiposervico) {
        this.idtiposervico = idtiposervico;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getFormapagamento() {
        return formapagamento;
    }

    public void setFormapagamento(int formapagamento) {
        this.formapagamento = formapagamento;
    }

}
