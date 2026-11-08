package br.com.avaliacao.devopscrud.produto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarProdutos() throws Exception {
        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void deveCriarProduto() throws Exception {
        String json = """
                {
                  "nome": "Webcam",
                  "descricao": "Webcam USB",
                  "preco": 199.90,
                  "estoque": 12
                }
                """;

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Webcam"));
    }

    @Test
    void deveRejeitarProdutoInvalido() throws Exception {
        String json = """
                {
                  "nome": "",
                  "descricao": "Produto inválido",
                  "preco": 0,
                  "estoque": -1
                }
                """;

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404ParaProdutoInexistente() throws Exception {
        mockMvc.perform(get("/api/produtos/99999"))
                .andExpect(status().isNotFound());
    }
}
