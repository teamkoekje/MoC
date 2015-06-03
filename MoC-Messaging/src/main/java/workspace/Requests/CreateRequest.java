/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace.Requests;

/**
 *
 * @author Luc
 */
public class CreateRequest extends TeamRequest {

    public CreateRequest(String competition, String teamNAme) {
        super(Action.CREATE, competition, teamNAme);
    }
}
