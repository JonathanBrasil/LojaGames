package com.generation.lojabign.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojabign.model.Produto;
import com.generation.lojabign.repository.CategoriaRepository;
import com.generation.lojabign.repository.ProdutoRepository;

@RestController
@RequestMapping ("/produtos")
@CrossOrigin (origins = "*", allowedHeaders = "*" )
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	//MÉTODOS GET
	
	//Find ALL produtos da classe(tabela) Model Produto
	@GetMapping 
	public ResponseEntity <List<Produto>> getAll(){
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	
	//Find All produtos por ID da classe(tabela) Model Produto
	@GetMapping ("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable long id){
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());

	}
	
	//Find All produtos por NOME da classe(tabela) Model Produto
	@GetMapping ("/nome/{nome}")
	public ResponseEntity<List<Produto>> getByNome (@PathVariable String nome){
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
		
	}
	
	//Find All produtos por  Preço <   
	@GetMapping ("/preco/maior/{preco}")
	public ResponseEntity<List<Produto>> getByPrecoMaior (@PathVariable BigDecimal preco){
		return ResponseEntity.ok(produtoRepository.findByPrecoGreaterThan(preco));
			
	}
	
	//Find All produtos por Preço >
	@GetMapping ("/preco/menor/{preco}")
	public ResponseEntity<List<Produto>> getByPrecoMenor (@PathVariable BigDecimal preco){
	return ResponseEntity.ok(produtoRepository.findByPrecoLessThan(preco));
				
	}
	
	@GetMapping ("/preco/categoria/{id}")
	public ResponseEntity<List<Produto>> getByPrecoTipo (@Valid @PathVariable  Long id){
		return null;
		
		
		
	}
	
	
	
	//MÉTODO POST
	
	@PostMapping
	public ResponseEntity <Produto> postProduto (@Valid @RequestBody Produto produto){
		if (produtoRepository.existsById(produto.getCategoria().getId()))
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		
		
	}
	
	//MÉTODO PUT
	
	@PutMapping
	public ResponseEntity <Produto> putProduto (@Valid @RequestBody Produto produto){
		if (produtoRepository.existsById(produto.getId()) ) {
			
			if (categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));
			}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		
	}
	
	//MÉTODO DELETE
	
	@DeleteMapping("{id}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public void deleteProduto (@Valid @PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		
		if (produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		produtoRepository.deleteById(id);
		
	}
	
	
	

}
