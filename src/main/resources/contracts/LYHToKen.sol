// SPDX-License-Identifier: MIT
// OpenZeppelin Contracts (last updated v5.0.1) (utils/Context.sol)

pragma solidity ^0.8.20;
import {IERC20} from "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import {IERC20Metadata} from "@openzeppelin/contracts/token/ERC20/extensions/IERC20Metadata.sol";
import "@openzeppelin/contracts/utils/Context.sol";
import {IERC20Errors} from "@openzeppelin/contracts/interfaces/draft-IERC6093.sol";

contract LYHToKen is IERC20,Context,IERC20Errors,IERC20Metadata {
    mapping(address account => uint256) private _balances;

    mapping(address account => mapping(address spender => uint256)) private _allowances;

    uint256 private _totalSupply;

    string private _name;
    string private _symbol;
    //变量声明参考github上的参考代码
   function _approve(address owner, address spender, uint256 value) internal {
        _approve(owner, spender, value, true);
    }
     function _approve(address owner, address spender, uint256 value, bool emitEvent) internal virtual {
        if (owner == address(0)) {
            revert ERC20InvalidApprover(address(0));
        }
        if (spender == address(0)) {
            revert ERC20InvalidSpender(address(0));
        }
        _allowances[owner][spender] = value;
        if (emitEvent) {
            emit Approval(owner, spender, value);
        }
    }
    function name() public view returns (string memory)
    {
        return _name;//返回代币的名称
    }
    function symbol() public view returns (string memory)
    {
        return _symbol;//返回代币缩写符号
    }
    function decimals() public pure returns (uint8) {
        return 18;
    }

    function totalSupply() public view returns (uint256 )
    {
        return _totalSupply;//代币发行总量
    }
    function balanceOf(address _owner) public view returns (uint256 balance) 
    {
        return _balances[_owner];//查询某个地址下的用户的代币总量
    }
     function transfer(address to, uint256 value) public virtual returns (bool) {
        address owner = _msgSender();
        _transfer(owner, to, value);
        return true;
    }
    function transferFrom(address _from, address _to, uint256 _value) public returns (bool success) 
    {   
        address spender = _to;
        _spendAllowance(_from, spender, _value);//先进行验证
        _transfer(_from, _to, _value);
        return true;
    }//从from地址传输到to地址
    function approve(address _spender, uint256 _value) public returns (bool success) 
    {  
        address owner = _msgSender();
        _approve(owner, _spender, _value,true);//调用内部函数
        return true;
    }
    function allowance(address _owner, address _spender) public view returns (uint256 remaining) 
    {
         return _allowances[_owner][_spender];//返回spender可以用owner多少货币
    }
  
    function _transfer(address from, address to, uint256 value) internal {
       
        _update(from, to, value);
        emit Transfer(from, to, value);
    }
    function _spendAllowance(address owner, address spender, uint256 value) internal virtual {
        uint256 currentAllowance = allowance(owner, spender);
        if (currentAllowance < type(uint256).max) {
            if (currentAllowance < value) {
                revert ERC20InsufficientAllowance(spender, currentAllowance, value);
            }
            unchecked {
                _approve(owner, spender, currentAllowance - value, false);
            }
        }
    }
     function _mint(address account, uint256 value) internal   {
        if (account == address(0)) {
            revert ERC20InvalidReceiver(address(0));
        }
        _update(address(0), account, value);
    }
    function mint(address account,uint256 value) public {
        _mint(account,value);
    }
     function _burn(address account, uint256 value) public payable   {
        if (account == address(0)) {
            revert ERC20InvalidSender(address(0));
        }
        _update(account, address(0), value);
    }
     function _update(address from, address to, uint256 value) internal virtual {
        if (from == address(0)) {
            // Overflow check required: The rest of the code assumes that totalSupply never overflows
            _totalSupply += value;
            _balances[to] += value;
        
        } else {
            uint256 fromBalance = _balances[from];
            if (fromBalance < value) {
                 revert ERC20InsufficientBalance(from, fromBalance, value);
             }
            unchecked {
                // Overflow not possible: value <= fromBalance <= totalSupply.
                _balances[from] = fromBalance - value;
                _balances[to]=_balances[to]+value;
                
            }
        }
    }

}